package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import models.AIState
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErr
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErrWithData
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseOk


fun ICorChainDsl<AppContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == AIState.RUNNING }
    handle {
        val request = DbModelRequest(modelRepoPrepare)
        when(val result = modelRepo.updateModel(request)) {
            is DbModelResponseOk -> modelRepoDone = result.data
            is DbModelResponseErr -> fail(result.errors)
            is DbModelResponseErrWithData -> {
                fail(result.errors)
                modelRepoDone = result.data
            }
        }
    }
}
