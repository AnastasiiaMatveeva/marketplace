package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import ru.otus.otuskotlin.aiassistant.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelIdRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErr
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErrWithData
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseOk

fun ICorChainDsl<AppContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == AIState.RUNNING }
    handle {
        val request = DbModelIdRequest(modelRepoPrepare)
        when(val result = modelRepo.deleteModel(request)) {
            is DbModelResponseOk -> modelRepoDone = result.data
            is DbModelResponseErr -> {
                fail(result.errors)
                modelRepoDone = modelRepoRead
            }
            is DbModelResponseErrWithData -> {
                fail(result.errors)
                modelRepoDone = result.data
            }
        }
    }
}
