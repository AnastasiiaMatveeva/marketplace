package ru.otus.otuskotlin.aiassistant.repo.stubs

import ru.otus.otuskotlin.aiassistant.common.repo.*
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub

class ModelRepoStub() : IRepoModel {
    override suspend fun createModel(rq: DbModelRequest): IDbModelResponse {
        return DbModelResponseOk(
            data = ModelStub.get(),
        )
    }

    override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse {
        return DbModelResponseOk(
            data = ModelStub.get(),
        )
    }

    override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse {
        return DbModelResponseOk(
            data = ModelStub.get(),
        )
    }

    override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse {
        return DbModelResponseOk(
            data = ModelStub.get(),
        )
    }

    override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse {
        return DbModelsResponseOk(
            data = ModelStub.prepareSearchList(filter = ""),
        )
    }
}
