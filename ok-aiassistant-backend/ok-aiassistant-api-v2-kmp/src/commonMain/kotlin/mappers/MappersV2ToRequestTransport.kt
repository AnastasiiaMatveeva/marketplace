package ru.otus.otuskotlin.aiassistant.mappers.v2

import models.AIModel
import models.AIModelId
import models.AIModelLock
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelCreateObject
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelDeleteObject
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelReadObject
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelUpdateObject

fun AIModel.toTransportCreate() = ModelCreateObject(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
)

fun AIModel.toTransportRead() = ModelReadObject(
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
)

fun AIModel.toTransportUpdate() = ModelUpdateObject(
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    lock = lock.takeIf { it != AIModelLock.NONE }?.asString(),
)

fun AIModel.toTransportDelete() = ModelDeleteObject(
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
    lock = lock.takeIf { it != AIModelLock.NONE }?.asString(),
)
