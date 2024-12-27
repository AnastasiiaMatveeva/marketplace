package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import models.AIState
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == AIState.RUNNING }
    handle {
        modelRepoPrepare = modelValidated.deepCopy()
    }
}
