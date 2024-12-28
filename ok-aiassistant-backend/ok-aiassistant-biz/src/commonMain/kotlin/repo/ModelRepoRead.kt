package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import models.AIState
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelIdRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErr
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErrWithData
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseOk

fun ICorChainDsl<AppContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение модели из БД"
    on { state == AIState.RUNNING }
    handle {
        val request = DbModelIdRequest(modelValidated)
        when(val result = modelRepo.readModel(request)) {
            is DbModelResponseOk -> {
                modelRepoRead = result.data
                modelRepoRead.features = modelValidated.features
            }
            is DbModelResponseErr -> fail(result.errors)
            is DbModelResponseErrWithData -> {
                fail(result.errors)
                modelRepoRead = result.data
            }
        }
    }
}
