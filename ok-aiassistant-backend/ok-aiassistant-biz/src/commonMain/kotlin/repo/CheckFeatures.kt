package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import ru.otus.otuskotlin.aiassistant.common.repo.errorBadParamValues
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.checkFeaturesExist(title: String) = worker {
    this.title = title
    on {
        state == AIState.RUNNING && modelValidated.features.size != modelRepoRead.modelParams.size }
    handle {
            fail(errorBadParamValues.errors)
            }
}
