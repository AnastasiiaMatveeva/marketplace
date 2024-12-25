package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.common.helpers.errorValidation

fun ICorChainDsl<AppContext>.validateParamPosition(title: String) = worker {
    this.title = title
    on { modelValidating.modelParams.any { it.position < 1 } }
    handle {
        fail(
            errorValidation(
                field = "position",
                violationCode = "negativePosition",
                description = "Position of parameter must be non-negative"
            )
        )
    }
}

