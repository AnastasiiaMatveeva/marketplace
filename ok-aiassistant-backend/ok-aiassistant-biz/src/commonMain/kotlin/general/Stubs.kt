package ru.otus.otuskotlin.aiassistant.biz.general

import AppContext
import models.AIState
import models.AIWorkMode
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.chain


fun ICorChainDsl<AppContext>.stubs(title: String, block: ICorChainDsl<AppContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == AIWorkMode.STUB && state == AIState.RUNNING }
}
