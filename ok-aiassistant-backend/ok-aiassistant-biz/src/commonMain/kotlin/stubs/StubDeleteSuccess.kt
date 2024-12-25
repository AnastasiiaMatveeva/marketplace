package ru.otus.otuskotlin.aiassistant.biz.stubs

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.logging.common.LogLevel
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import AppContext
import models.AIState
import models.AIModelLock
import stubs.AIStubs

fun ICorChainDsl<AppContext>.stubDeleteSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для удаления модели на базе ИИ
    """.trimIndent()
    on { stubCase == AIStubs.SUCCESS && state == AIState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubDeleteSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = AIState.FINISHING
            val stub = ModelStub.prepareResult {
                modelRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                modelRequest.lock.takeIf { it != AIModelLock.NONE }?.also { this.lock = it }
            }
            modelResponse = stub
        }
    }
}
