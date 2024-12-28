package ru.otus.otuskotlin.aiassistant.biz.stubs

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.logging.common.LogLevel
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import AppContext
import models.AIState
import models.AIModelId
import models.AIVisibility
import stubs.AIStubs
import models.AIModelLock

fun ICorChainDsl<AppContext>.stubUpdateSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для изменения модели на базе ИИ
    """.trimIndent()
    on { stubCase == AIStubs.SUCCESS && state == AIState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubUpdateccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = AIState.FINISHING
            val stub = ModelStub.prepareResult {
                modelRequest.id.takeIf { it != AIModelId.NONE }?.also { this.id = it }
                modelRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                modelRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                modelRequest.visibility.takeIf { it != AIVisibility.NONE }?.also { this.visibility = it }
                modelRequest.scriptPath.takeIf { it.isNotBlank() }?.also { this.scriptPath = it }
                modelRequest.solverPath.takeIf { it.isNotBlank() }?.also { this.solverPath = it }
                modelRequest.modelParams.takeIf { it.isNotEmpty() }?.also { this.modelParams = it }
                modelRequest.lock.takeIf { it != AIModelLock.NONE }?.also { this.lock = it }
            }
            modelResponse = stub
        }
    }
}
