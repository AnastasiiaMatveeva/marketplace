package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import AppContext
import models.AIState
import ru.otus.otuskotlin.aiassistant.cor.chain

fun ICorChainDsl<AppContext>.validation(block: ICorChainDsl<AppContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == AIState.RUNNING }
}
