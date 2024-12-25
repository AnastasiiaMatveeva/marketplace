package ru.otus.otuskotlin.aiassistant.biz.stubs

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.logging.common.LogLevel
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import AppContext
import models.AIModelParam
import models.AIState
import models.AIVisibility
import models.AIUserId
import models.AIModelLock
import stubs.AIStubs

fun ICorChainDsl<AppContext>.stubCreateSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания модели на базе ИИ
    """.trimIndent()
    on { stubCase == AIStubs.SUCCESS && state == AIState.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = AIState.FINISHING
            val stub = ModelStub.prepareResult {
                modelRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
                modelRequest.description.takeIf { it.isNotBlank() }?.also { this.description = it }
                modelRequest.scriptPath.takeIf { it.isNotBlank() }?.also { this.scriptPath = it }
                modelRequest.solverPath.takeIf { it.isNotBlank() }?.also { this.solverPath = it }
                modelRequest.modelParams.takeIf { it.validate().isNotEmpty() }?.also { this.modelParams = it }
                modelRequest.features.takeIf { it.isNotEmpty() }?.also { this.features = it }
                modelRequest.ownerId.takeIf { it != AIUserId.NONE }?.also { this.ownerId = it }
                modelRequest.visibility.takeIf { it != AIVisibility.NONE }?.also { this.visibility = it }
                modelRequest.permissionsClient.takeIf { it.isNotEmpty() }?.also { this.permissionsClient.addAll(it) }
                modelRequest.lock.takeIf { it != AIModelLock.NONE }?.also { this.lock = it }
            }
            modelResponse = stub
        }
    }
}

private fun List<AIModelParam>.validate(): MutableList<AIModelParam> =
    this.filter { it.line != 0 ||
                it.position != 0 ||
                it.separator.isNotBlank() ||
                it.name.isNotBlank() ||
                it.bounds.isEmpty()
    }.toMutableList()

