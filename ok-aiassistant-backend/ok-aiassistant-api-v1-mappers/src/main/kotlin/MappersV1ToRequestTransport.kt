package ru.otus.otuskotlin.aiassistant.mappers.v1

import models.*
import ru.otus.otuskotlin.aiassistant.api.v1.models.*

fun AIModel.toTransportCreate() = ModelCreateObject(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    scriptPath = scriptPath.takeIf { it.isNotBlank() },
    solverPath = solverPath.takeIf { it.isNotBlank() },
    params = modelParams.takeIf { it.isNotEmpty() }?.map { it.toParam() },
    visibility = visibility.toTransportModel(),
)

fun AIModel.toTransportRead() = ModelReadObject(
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
)

fun AIModel.toTransportUpdate() = ModelUpdateObject(
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    scriptPath = scriptPath.takeIf { it.isNotBlank() },
    solverPath = solverPath.takeIf { it.isNotBlank() },
    params = modelParams.takeIf { it.isNotEmpty() }?.map { it.toParam() },
    visibility = visibility.toTransportModel(),
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
    lock = lock.takeIf { it != AIModelLock.NONE }?.asString(),
)

fun AIModel.toTransportDelete() = ModelDeleteObject(
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
    lock = lock.takeIf { it != AIModelLock.NONE }?.asString(),
)

fun AIModel.toTransportTrain() = ModelTrainObject(
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
    lock = lock.takeIf { it != AIModelLock.NONE }?.asString(),

    )

fun AIModel.toTransportPredict() = ModelPredictObject(
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
    lock = lock.takeIf { it != AIModelLock.NONE }?.asString(),
    features = features.takeIf { it.isNotEmpty() }?.toList()
)

fun AIModelParam.toParam() = ModelParam(
    paramType = paramType.toTransportModel(),
    line = line.takeIf { it != 0 },
    position = position.takeIf { it != 0 },
    separator = separator.takeIf { it.isNotBlank() },
    name = name.takeIf { it.isNotBlank() },
    bounds = bounds.toTransportBounds()
)

private fun ParamBounds.toTransportBounds(): Bounds = Bounds(
    min = min.takeIf { !it.isNaN() },
    max = max.takeIf { !it.isNaN() }
)
