package ru.otus.otuskotlin.aiassistant.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import models.*

class ModelTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val solver_path = text(SqlFields.SOLVER_PATH).nullable()
    val script_path = text(SqlFields.SCRIPT_PATH).nullable()
    val owner = text(SqlFields.OWNER_ID)
    val visibility = visibilityEnumeration(SqlFields.VISIBILITY)
    val lock = text(SqlFields.LOCK)
    val features = array<Double>(SqlFields.FEATURES).nullable()
    val results = array<Double>(SqlFields.RESULTS).nullable()

    override val primaryKey = PrimaryKey(id)



    fun from(res: ResultRow): AIModel {
        return AIModel(
            id = AIModelId(res[id].toString()),
            title = res[title] ?: "",
            description = res[description] ?: "",
            ownerId = AIUserId(res[owner].toString()),
            solverPath = res[solver_path] ?: "",
            scriptPath = res[script_path] ?: "",
            visibility = res[visibility],
            features = res[features]?.toTypedArray() ?: arrayOf(),
            results = res[results]?.toTypedArray() ?: arrayOf(),
            lock = AIModelLock(res[lock]),
            )
    }


    fun to(it: UpdateBuilder<*>, model: AIModel, randomUuid: () -> String) {
        it[id] = model.id.takeIf { it != AIModelId.NONE }?.asString() ?: randomUuid()
        it[title] = model.title
        it[description] = model.description
        it[owner] = model.ownerId.asString()
        it[solver_path] = model.solverPath
        it[script_path] = model.scriptPath
        it[visibility] = model.visibility
        it[features] = model.features.toList()
        it[results] = model.results.toList()
        it[lock] = model.lock.takeIf { it != AIModelLock.NONE }?.asString() ?: randomUuid()
    }

}

