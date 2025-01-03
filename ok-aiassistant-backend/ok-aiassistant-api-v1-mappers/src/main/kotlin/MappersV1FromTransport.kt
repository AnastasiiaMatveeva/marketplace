package ru.otus.otuskotlin.aiassistant.mappers.v1

import AppContext
import models.*
import models.AIParamType
import stubs.AIStubs
import ru.otus.otuskotlin.aiassistant.api.v1.models.*
import exceptions.UnknownRequestClass


fun AppContext.fromTransport(request: IRequest) = when (request) {
    is ModelCreateRequest -> fromTransport(request)
    is ModelReadRequest -> fromTransport(request)
    is ModelUpdateRequest -> fromTransport(request)
    is ModelDeleteRequest -> fromTransport(request)
    is ModelSearchRequest -> fromTransport(request)
    is ModelTrainRequest -> fromTransport(request)
    is ModelPredictRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toModelId() = this?.let { AIModelId(it) } ?: AIModelId.NONE
private fun String?.toModelLock() = this?.let { AIModelLock(it) } ?: AIModelLock.NONE

private fun ModelDebug?.transportToWorkMode(): AIWorkMode = when (this?.mode) {
    ModelRequestDebugMode.PROD -> AIWorkMode.PROD
    ModelRequestDebugMode.TEST -> AIWorkMode.TEST
    ModelRequestDebugMode.STUB -> AIWorkMode.STUB
    null -> AIWorkMode.PROD
}

private fun ModelDebug?.transportToStubCase(): AIStubs = when (this?.stub) {
    ModelRequestDebugStubs.SUCCESS -> AIStubs.SUCCESS
    ModelRequestDebugStubs.NOT_FOUND -> AIStubs.NOT_FOUND
    ModelRequestDebugStubs.BAD_ID -> AIStubs.BAD_ID
    ModelRequestDebugStubs.BAD_TITLE -> AIStubs.BAD_TITLE
    ModelRequestDebugStubs.BAD_USER_ID -> AIStubs.BAD_USER_ID
    ModelRequestDebugStubs.BAD_DESCRIPTION -> AIStubs.BAD_DESCRIPTION
    ModelRequestDebugStubs.BAD_SCRIPT_PATH -> AIStubs.BAD_SCRIPT_PATH
    ModelRequestDebugStubs.BAD_PARAM_POSITION -> AIStubs.BAD_PARAM_POSITION
    ModelRequestDebugStubs.BAD_PARAM_BOUNDS -> AIStubs.BAD_PARAM_BOUNDS
    ModelRequestDebugStubs.BAD_PARAM_TYPE -> AIStubs.BAD_PARAM_TYPE
    ModelRequestDebugStubs.BAD_FEATURES -> AIStubs.BAD_FEATURES
    ModelRequestDebugStubs.BAD_VISIBILITY -> AIStubs.BAD_VISIBILITY
    ModelRequestDebugStubs.BAD_PERMISSION -> AIStubs.BAD_PERMISSION
    ModelRequestDebugStubs.CANNOT_DELETE -> AIStubs.CANNOT_DELETE
    ModelRequestDebugStubs.BAD_SEARCH_STRING -> AIStubs.BAD_SEARCH_STRING
    ModelRequestDebugStubs.BAD_LOCK-> AIStubs.BAD_LOCK
    ModelRequestDebugStubs.DB_ERROR-> AIStubs.DB_ERROR
    null -> AIStubs.NONE
}

fun AppContext.fromTransport(request: ModelCreateRequest) {
    command = AICommand.CREATE
    modelRequest = request.model?.toInternal() ?: AIModel()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: ModelReadRequest) {
    command = AICommand.READ
    modelRequest = request.model.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: ModelUpdateRequest) {
    command = AICommand.UPDATE
    modelRequest = request.model?.toInternal() ?: AIModel()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: ModelDeleteRequest) {
    command = AICommand.DELETE
    modelRequest = request.model.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: ModelSearchRequest) {
    command = AICommand.SEARCH
    modelFilterRequest = request.modelFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: ModelTrainRequest) {
    command = AICommand.TRAIN
    modelRequest = request.model?.toInternal() ?: AIModel()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: ModelPredictRequest) {
    command = AICommand.PREDICT
    modelRequest = request.model?.toInternal() ?: AIModel()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ModelReadObject?.toInternal(): AIModel = if (this != null) {
    AIModel(id = id.toModelId())
} else {
    AIModel()
}

private fun ModelDeleteObject?.toInternal(): AIModel = if (this != null) {
    AIModel(
        id = id.toModelId(),
        lock = lock.toModelLock(),
    )
} else {
    AIModel()
}

private fun ModelSearchFilter?.toInternal(): AIModelFilter = AIModelFilter(
    searchString = this?.searchString ?: "",
    ownerId = this?.ownerId?.let { AIUserId(it) } ?: AIUserId.NONE,
    )

private fun ModelTrainObject.toInternal(): AIModel = AIModel(
    id = id.toModelId(),
    lock = lock.toModelLock()
)

private fun ModelPredictObject.toInternal(): AIModel = AIModel(
    id = id.toModelId(),
    lock = lock.toModelLock(),
    features = features?.toTypedArray() ?: emptyArray(),
)

private fun ModelCreateObject.toInternal(): AIModel = AIModel(
    title = this.title ?: "",
    description = this.description ?: "",
    modelParams = this.params?.map { it.toInternal() }?.toMutableList() ?: mutableListOf(),
    solverPath = this.solverPath ?: "",
    scriptPath = this.scriptPath ?: "",
    visibility = this.visibility.fromTransport(),
)

private fun ModelUpdateObject.toInternal(): AIModel = AIModel(
    id = this.id.toModelId(),
    title = this.title ?: "",
    description = this.description ?: "",
    modelParams = this.params?.map { it.toInternal() }?.toMutableList() ?: mutableListOf(),
    solverPath = this.solverPath ?: "",
    scriptPath = this.scriptPath ?: "",
    visibility = this.visibility.fromTransport(),
    lock = lock.toModelLock(),
)

private fun ModelVisibility?.fromTransport(): AIVisibility = when (this) {
    ModelVisibility.PUBLIC -> AIVisibility.VISIBLE_PUBLIC
    ModelVisibility.OWNER_ONLY -> AIVisibility.VISIBLE_TO_OWNER
    ModelVisibility.REGISTERED_ONLY -> AIVisibility.VISIBLE_TO_GROUP
    null -> AIVisibility.NONE
}

private fun ModelParamType?.fromTransport(): AIParamType = when (this) {
    ModelParamType.CONTINUOUS -> AIParamType.CONTINUOUS
    ModelParamType.DISCRETE -> AIParamType.DISCRETE
    null -> AIParamType.NONE
}

private fun ModelParam?.toInternal(): AIModelParam = if (this != null) {
    AIModelParam(
        paramType = this.paramType.fromTransport(),
        bounds = ParamBounds(
            min = this.bounds?.min ?: Double.NaN,
            max = this.bounds?.max ?: Double.NaN
        ),
        line = this.line ?: 0,
        position = this.position ?: 0,
        separator = this.separator ?: "",
        name = this.name ?: ""
    )
} else {
    AIModelParam()
}





