package ru.otus.otuskotlin.aiassistant.repo.postgresql

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.otus.otuskotlin.aiassistant.common.helpers.asError
import models.AIModel
import models.AIModelId
import models.AIModelLock
import models.AIUserId
import ru.otus.otuskotlin.aiassistant.common.repo.*
import ru.otus.otuskotlin.aiassistant.repo.common.IRepoModelInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoModelSql actual constructor(
    properties: SqlProperties,
    private val randomUuid: () -> String
) : IRepoModel, IRepoModelInitializable {
    private val modelTable = ModelTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    actual fun clear(): Unit = transaction(conn) {
        modelTable.deleteAll()
    }

    private fun saveObj(model: AIModel): AIModel = transaction(conn) {
        val res = modelTable
            .insert {
                to(it, model, randomUuid)
            }
            .resultedValues
            ?.map { modelTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbModelResponse): IDbModelResponse =
        transactionWrapper(block) { DbModelResponseErr(it.asError()) }

    actual override fun save(models: Collection<AIModel>): Collection<AIModel> = models.map { saveObj(it) }
    actual override suspend fun createModel(rq: DbModelRequest): IDbModelResponse = transactionWrapper {
        DbModelResponseOk(saveObj(rq.model))
    }

    private fun read(id: AIModelId): IDbModelResponse {
        val res = modelTable.selectAll().where {
            modelTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)
        return DbModelResponseOk(modelTable.from(res))
    }

    actual override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse = transactionWrapper { read(rq.id) }

    private suspend fun update(
        id: AIModelId,
        lock: AIModelLock,
        block: (AIModel) -> IDbModelResponse
    ): IDbModelResponse =
        transactionWrapper {
            if (id == AIModelId.NONE) return@transactionWrapper errorEmptyId

            val current = modelTable.selectAll().where { modelTable.id eq id.asString() }
                .singleOrNull()
                ?.let { modelTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }


    actual override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse = update(rq.model.id, rq.model.lock) {
        modelTable.update({ modelTable.id eq rq.model.id.asString() }) {
            to(it, rq.model.copy(lock = AIModelLock(randomUuid())), randomUuid)
        }
        read(rq.model.id)
    }

    actual override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse = update(rq.id, rq.lock) {
        modelTable.deleteWhere { id eq rq.id.asString() }
        DbModelResponseOk(it)
    }

    actual override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse =
        transactionWrapper({
            val res = modelTable.selectAll().where {
                buildList {
                    add(Op.TRUE)
                    if (rq.ownerId != AIUserId.NONE) {
                        add(modelTable.owner eq rq.ownerId.asString())
                    }
                    if (rq.titleFilter.isNotBlank()) {
                        add(
                            (modelTable.title like "%${rq.titleFilter}%")
                                    or (modelTable.description like "%${rq.titleFilter}%")
                        )
                    }
                }.reduce { a, b -> a and b }
            }
            DbModelsResponseOk(data = res.map { modelTable.from(it) })
        }, {
            DbModelsResponseErr(it.asError())
        })
}
