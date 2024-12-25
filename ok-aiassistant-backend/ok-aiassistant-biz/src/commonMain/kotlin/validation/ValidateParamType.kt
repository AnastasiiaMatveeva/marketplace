package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import AppContext
import models.AIParamType
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.common.helpers.errorValidation

// смотрим пример COR DSL валидации
fun ICorChainDsl<AppContext>.validateParamType(title: String) = worker {
    this.title = title
    on { modelValidating.modelParams.any { it.paramType == AIParamType.NONE } }
    handle {
        fail(
            errorValidation(
                field = "paramType",
                violationCode = "missingType",
                description = "Parameter type must be specified"
            )
        )
    }
}