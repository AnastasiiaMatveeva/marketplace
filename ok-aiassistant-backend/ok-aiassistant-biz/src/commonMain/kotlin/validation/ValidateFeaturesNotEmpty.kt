package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.common.helpers.errorValidation

fun ICorChainDsl<AppContext>.validateFeaturesNotEmpty(title: String) = worker {
    this.title = title
    on {
        modelValidating.features.isEmpty()}
    handle {
        fail(
            errorValidation(
                field = "features",
                violationCode = "empty",
                description = "features must not be empty"
            )
        )
    }
}

