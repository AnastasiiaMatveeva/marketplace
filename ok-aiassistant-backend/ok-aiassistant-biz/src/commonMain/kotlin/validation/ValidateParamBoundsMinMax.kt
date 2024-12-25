package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.common.helpers.errorValidation

// смотрим пример COR DSL валидации
fun ICorChainDsl<AppContext>.validateParamBoundsMinMax(title: String) = worker {
    this.title = title
    on {
        modelValidating.modelParams.any { param ->
            val bounds = param.bounds
            !bounds.min.isNaN() && !bounds.max.isNaN() && bounds.min > bounds.max
        }
    }
    handle {
        fail(
            errorValidation(
                field = "bounds",
                violationCode = "invalidBounds",
                description = "Minimum bound cannot be greater than maximum bound"
            )
        )
    }
}

