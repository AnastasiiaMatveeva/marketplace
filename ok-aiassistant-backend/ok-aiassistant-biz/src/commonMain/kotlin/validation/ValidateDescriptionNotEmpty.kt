package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.common.helpers.errorValidation

fun ICorChainDsl<AppContext>.validateDescriptionNotEmpty(title: String) = worker {
    this.title = title
    on { modelValidating.description.isEmpty() }
    handle {
        fail(
            errorValidation(
            field = "description",
            violationCode = "empty",
            description = "field must not be empty"
        )
        )
    }
}
