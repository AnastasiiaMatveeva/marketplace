package ru.otus.otuskotlin.aiassistant.common.repo

import models.AIModelId
import models.AIModelLock
import models.AIModel

data class DbModelIdRequest(
    val id: AIModelId,
    val lock: AIModelLock = AIModelLock.NONE,
) {
    constructor(model: AIModel): this(model.id, model.lock)
}
