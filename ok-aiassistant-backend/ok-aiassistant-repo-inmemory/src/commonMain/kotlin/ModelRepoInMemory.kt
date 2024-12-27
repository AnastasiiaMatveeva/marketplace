package ru.otus.otuskotlin.aiassistant.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import models.AIModel
import models.AIModelId
import models.AIModelLock
import models.AIUserId
import ru.otus.otuskotlin.aiassistant.repo.common.*
import ru.otus.otuskotlin.aiassistant.common.repo.*
import ru.otus.otuskotlin.aiassistant.common.repo.exceptions.RepoEmptyLockException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class ModelRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : ModelRepoBase(), IRepoModel, IRepoModelInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, ModelEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(models: Collection<AIModel>) = models.map { model ->
        val entity = ModelEntity(model)
        require(entity.id != null)
        cache.put(entity.id, entity)
        model
    }

    override suspend fun createModel(rq: DbModelRequest): IDbModelResponse = tryModelMethod {
        val key = randomUuid()
        val model = rq.model.copy(id = AIModelId(key), lock = AIModelLock(randomUuid()))
        val entity = ModelEntity(model)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbModelResponseOk(model)
    }

    override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse = tryModelMethod {
        val key = rq.id.takeIf { it != AIModelId.NONE }?.asString() ?: return@tryModelMethod errorEmptyId
        mutex.withLock {
            cache.get(key)
                ?.let {
                    DbModelResponseOk(it.toInternal())
                } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse = tryModelMethod {
        val rqModel = rq.model
        val id = rqModel.id.takeIf { it != AIModelId.NONE } ?: return@tryModelMethod errorEmptyId
        val key = id.asString()
        val oldLock = rqModel.lock.takeIf { it != AIModelLock.NONE } ?: return@tryModelMethod errorEmptyLock(id)

        mutex.withLock {
            val oldModel = cache.get(key)?.toInternal()
            when {
                oldModel == null -> errorNotFound(id)
                oldModel.lock == AIModelLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldModel.lock != oldLock -> errorRepoConcurrency(oldModel, oldLock)
                else -> {
                    val newModel = rqModel.copy(lock = AIModelLock(randomUuid()))
                    val entity = ModelEntity(newModel)
                    cache.put(key, entity)
                    DbModelResponseOk(newModel)
                }
            }
        }
    }


    override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse = tryModelMethod {
        val id = rq.id.takeIf { it != AIModelId.NONE } ?: return@tryModelMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != AIModelLock.NONE } ?: return@tryModelMethod errorEmptyLock(id)

        mutex.withLock {
            val oldModel = cache.get(key)?.toInternal()
            when {
                oldModel == null -> errorNotFound(id)
                oldModel.lock == AIModelLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldModel.lock != oldLock -> errorRepoConcurrency(oldModel, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbModelResponseOk(oldModel)
                }
            }
        }
    }

    override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse = tryModelsMethod {
        println("Received DbModelFilterRequest: ownerId=${rq.ownerId}, titleFilter=${rq.titleFilter}")

        val result: List<AIModel> = cache.asMap().asSequence()
            .onEach { println("Before filtering: ${it.key} -> ${it.value}") }
            .filter { entry ->
                rq.ownerId.takeIf { it != AIUserId.NONE }?.let {
                    it.asString() == entry.value.ownerId
                } ?: true
            }
            .onEach { println("After ownerId filtering: ${it.key} -> ${it.value}") }
            .filter { entry ->
                rq.titleFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.title?.contains(it) ?: false
                } ?: true
            }
            .onEach { println("After titleFilter filtering: ${it.key} -> ${it.value}") }
            .map { it.value.toInternal() }
            .toList()
        println("Final result: $result")
        DbModelsResponseOk(result)
    }
}
