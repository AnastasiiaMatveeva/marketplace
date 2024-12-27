package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import models.AIState
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelFilterRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelsResponseErr
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelsResponseOk

fun ICorChainDsl<AppContext>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск объявлений в БД по фильтру"
    on { state == AIState.RUNNING }
    handle {
        val request = DbModelFilterRequest(
            titleFilter = modelFilterValidated.searchString,
            ownerId = modelFilterValidated.ownerId,
        )
        when(val result = modelRepo.searchModel(request)) {
            is DbModelsResponseOk -> modelsRepoDone = result.data.toMutableList()
            is DbModelsResponseErr -> fail(result.errors)
        }
    }
}
