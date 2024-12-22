package ru.otus.otuskotlin.aiassistant.api.log1.mapper

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.aiassistant.api.log1.models.*
import AppContext
import models.*

fun AppContext.toLog(logId: String) = CommonLogSchema(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "ok-aiassistant",
    model = toLog(),
    errors = errors.map { it.toLog() },
)

private fun AppContext.toLog(): ModelLogSchema? {
    val AImodelNone = AIModel()
    return ModelLogSchema(
        requestId = requestId.takeIf { it != AIRequestId.NONE }?.asString(),
        requestModel = modelRequest.takeIf { it != AImodelNone }?.toLog(),
        responseModel = modelResponse.takeIf { it != AImodelNone }?.toLog(),
        responseModels = modelsResponse.takeIf { it.isNotEmpty() }?.filter { it != AImodelNone }?.map { it.toLog() },
        requestFilter = modelFilterRequest.takeIf { it != AIModelFilter() }?.toLog(),
    ).takeIf { it != ModelLogSchema() }
}

private fun AIModelFilter.toLog() = ModelFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != AIUserId.NONE }?.asString(),
)

private fun AIError.toLog() = ErrorLogSchema(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun AIModel.toLog() = ModelLog(
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    scriptPath = description.takeIf { it.isNotBlank() },
    solverPath = description.takeIf { it.isNotBlank() },
    visibility = visibility.takeIf { it != AIVisibility.NONE }?.name,
    ownerId = ownerId.takeIf { it != AIUserId.NONE }?.asString(),
    permissions = permissionsClient.takeIf { it.isNotEmpty() }?.map { it.name }?.toSet(),
    features = features.takeIf { it.isNotEmpty() }?.toList(),
    results = results.takeIf { it.isNotEmpty() }?.toList(),
    params = modelParams.takeIf { it.isNotEmpty() }?.map { it.toParamLog() }?.toList(),
)

private fun AIModelParam.toParamLog(): ParamLog = ParamLog(
    paramType = paramType.takeIf { it != AIParamType.NONE }?.name,
    bounds = bounds.toBoundsLog(),
    line = line.takeIf { it != 0 },
    position = position.takeIf { it != 0 },
    separator = separator.takeIf { it.isNotBlank() },
    name = name.takeIf { it.isNotBlank() },
)

private fun ParamBounds.toBoundsLog(): BoundsLog = BoundsLog(
    min = min.takeIf { !it.isNaN() },
    max = max.takeIf { !it.isNaN() },
)