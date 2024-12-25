package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.common.helpers.errorValidation

fun ICorChainDsl<AppContext>.validateFeatures(title: String) = worker {
    this.title = title
    on {
        modelValidating.features.size != modelValidating.modelParams.size}
    handle {
        fail(
            errorValidation(
                field = "features",
                violationCode = "invalidCount",
                description = "Number of features must match the number of model parameters"
            )
        )
    }
}

