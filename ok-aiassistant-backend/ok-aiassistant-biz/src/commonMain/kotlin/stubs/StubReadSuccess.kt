package ru.otus.otuskotlin.aiassistant.biz.stubs

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.logging.common.LogLevel
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import AppContext
import models.AIState
import models.AIModelId
import stubs.AIStubs

fun ICorChainDsl<AppContext>.stubReadSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для чтения модели на базе ИИ
    """.trimIndent()
    on { stubCase == AIStubs.SUCCESS && state == AIState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubReadSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = AIState.FINISHING
            val stub = ModelStub.prepareResult {
                modelRequest.id.takeIf { it != AIModelId.NONE }?.also { this.id = it }
            }
            modelResponse = stub
        }
    }
}
