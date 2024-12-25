package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import AppContext
import models.AIState


fun ICorChainDsl<AppContext>.finishModelValidation(title: String) = worker {
    this.title = title
    on { state == AIState.RUNNING }
    handle {
        modelValidated = modelValidating
    }
}

fun ICorChainDsl<AppContext>.finishModelFilterValidation(title: String) = worker {
    this.title = title
    on { state == AIState.RUNNING }
    handle {
        modelFilterValidated = modelFilterValidating
    }
}
