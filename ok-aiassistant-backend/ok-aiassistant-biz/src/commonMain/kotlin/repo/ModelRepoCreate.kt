package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import ru.otus.otuskotlin.aiassistant.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErr
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErrWithData
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseOk

fun ICorChainDsl<AppContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление модели в БД"
    on { state == AIState.RUNNING }
    handle {
        val request = DbModelRequest(modelRepoPrepare)
        when(val result = modelRepo.createModel(request)) {
            is DbModelResponseOk -> modelRepoDone = result.data
            is DbModelResponseErr -> fail(result.errors)
            is DbModelResponseErrWithData -> fail(result.errors)
        }
    }
}
