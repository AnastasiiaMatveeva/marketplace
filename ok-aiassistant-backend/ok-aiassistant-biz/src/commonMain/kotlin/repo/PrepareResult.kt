package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import models.AIWorkMode
import ru.otus.otuskotlin.aiassistant.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != AIWorkMode.STUB }
    handle {
        modelResponse = modelRepoDone
        modelsResponse = modelsRepoDone
        state = when (val st = state) {
            AIState.RUNNING -> AIState.FINISHING
            else -> st
        }
    }
}
