package ru.otus.otuskotlin.aiassistant.repo.postgresql

import com.benasher44.uuid.uuid4
import models.AIModel
import ru.otus.otuskotlin.aiassistant.common.repo.*
import ru.otus.otuskotlin.aiassistant.repo.common.IRepoModelInitializable

//@Suppress("NO_ACTUAL_FOR_EXPECT")
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class RepoModelSql(
    properties: SqlProperties,
    randomUuid: () -> String = { uuid4().toString() },
) : IRepoModel, IRepoModelInitializable {
    override fun save(models: Collection<AIModel>): Collection<AIModel>
    override suspend fun createModel(rq: DbModelRequest): IDbModelResponse
    override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse
    override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse
    override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse
    override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse
    fun clear()
}
