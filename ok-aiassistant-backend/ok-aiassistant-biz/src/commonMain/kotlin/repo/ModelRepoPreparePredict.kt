package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import models.AIState
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.repoPreparePredict(title: String) = worker {
    this.title = title
    description = "Прогноз результатов с помощью модели"
    on { state == AIState.RUNNING }
    handle {
        modelRepoPrepare = modelRepoRead.deepCopy().apply {
            this.results = arrayOf(0.4, 0.5)
        }
    }
}
