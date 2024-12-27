package ru.otus.otuskotlin.aiassistant.repo.tests

import models.*

abstract class BaseInitModels(private val op: String): IInitObjects<AIModel> {
    open val lockOld: AIModelLock = AIModelLock("20000000-0000-0000-0000-000000000001")
    open val lockBad: AIModelLock = AIModelLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        ownerId: AIUserId = AIUserId("owner-123"),
        lock: AIModelLock = lockOld,
    ) = AIModel(
        id = AIModelId("model-repo-$op-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId,
        lock = lock,
        visibility = AIVisibility.VISIBLE_TO_OWNER
    )
}
