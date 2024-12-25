package ru.otus.otuskotlin.aiassistant.biz.general

import AppContext
import models.AICommand
import models.AIState
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.chain

fun ICorChainDsl<AppContext>.operation(
    title: String,
    command: AICommand,
    block: ICorChainDsl<AppContext>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == AIState.RUNNING }
}
