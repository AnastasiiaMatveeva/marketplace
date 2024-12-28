package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import ru.otus.otuskotlin.aiassistant.common.helpers.errorValidation
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.checkStatusTrain(title: String) = worker {
    this.title = title
    on {
        modelRepoRead.status.isEmpty()
    }
    handle {
        fail(
            errorValidation(
                field = "status",
                violationCode = "empty",
                description = "you must train model before predicting"
            )
        )
    }
}
