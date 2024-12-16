import models.*
import ru.otus.otuskotlin.aiassistant.api.v1.models.*


fun AppContext.toTransportModel(): IResponse = when (val cmd = command) {
    AICommand.CREATE -> toTransportCreate()
    AICommand.READ -> toTransportRead()
    AICommand.UPDATE -> toTransportUpdate()
    AICommand.DELETE -> toTransportDelete()
    AICommand.SEARCH -> toTransportSearch()
    AICommand.TRAIN -> toTransportTrain()
    AICommand.PREDICT -> toTransportPredict()
    AICommand.NONE -> throw UnknownCommand(cmd)
}

fun AppContext.toTransportCreate() = ModelCreateResponse(
    responseType = "create",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun AppContext.toTransportRead() = ModelReadResponse(
    responseType = "read",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun AppContext.toTransportUpdate() = ModelUpdateResponse(
    responseType = "update",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun AppContext.toTransportDelete() = ModelDeleteResponse(
    responseType = "delete",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun AppContext.toTransportSearch() = ModelSearchResponse(
    responseType = "search",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    models = modelsResponse.toTransportModel()
)

fun AppContext.toTransportTrain() = ModelTrainResponse(
    responseType = "train",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun AppContext.toTransportPredict() = ModelPredictResponse(
    responseType = "predict",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun List<AIModel>.toTransportModel(): List<ModelResponseObject>? = this
    .map { it.toTransportModel() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AIModel.toTransportModel(): ModelResponseObject = ModelResponseObject(
    id = id.takeIf { it != AIModelId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    params = modelParams.toTransportParams(),
    ownerId = ownerId.takeIf { it != AIUserId.NONE }?.asString(),
    features = features.takeIf { it.isNotEmpty() }?.toList(),
    results = results.takeIf { it.isNotEmpty() }?.toList(),
    scriptPath = scriptPath.takeIf { it.isNotBlank() },
    solverPath = solverPath.takeIf { it.isNotBlank() },
    visibility = visibility.toTransportModel(),
    lock = lock.takeIf { it != AIModelLock.NONE }?.asString(),
    permissions = permissionsClient.toTransportModel(),
)

private fun List<AIModelParam>.toTransportParams(): List<ModelParam>? = this
    .map { it.toTransportParam() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AIModelParam.toTransportParam(): ModelParam = ModelParam(
    paramType = paramType.toTransportModel(),
    line = line.takeIf { it != 0 },
    position = position.takeIf { it != 0 },
    separator = separator.takeIf { it.isNotBlank() },
    name = name.takeIf { it.isNotBlank() },
    bounds = bounds.toTransportBounds(),
)

private fun ParamBounds.toTransportBounds(): Bounds = Bounds(
    min = min.takeIf { !it.isNaN() } ?: Double.NaN,
    max = max.takeIf { !it.isNaN() } ?: Double.NaN
)

private fun AIParamType.toTransportModel(): ModelParamType? = when (this) {
    AIParamType.DISCRETE -> ModelParamType.DISCRETE
    AIParamType.CONTINUOUS -> ModelParamType.CONTINUOUS
    AIParamType.NONE -> null
}

private fun Set<AIModelPermissionClient>.toTransportModel(): Set<ModelPermissions>? = this
    .map { it.toTransportModel() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun AIModelPermissionClient.toTransportModel() = when (this) {
    AIModelPermissionClient.READ -> ModelPermissions.READ
    AIModelPermissionClient.UPDATE -> ModelPermissions.UPDATE
    AIModelPermissionClient.MAKE_VISIBLE_OWNER -> ModelPermissions.MAKE_VISIBLE_OWN
    AIModelPermissionClient.MAKE_VISIBLE_GROUP -> ModelPermissions.MAKE_VISIBLE_GROUP
    AIModelPermissionClient.MAKE_VISIBLE_PUBLIC -> ModelPermissions.MAKE_VISIBLE_PUBLIC
    AIModelPermissionClient.DELETE -> ModelPermissions.DELETE
}

private fun AIVisibility.toTransportModel(): ModelVisibility? = when (this) {
    AIVisibility.VISIBLE_PUBLIC -> ModelVisibility.PUBLIC
    AIVisibility.VISIBLE_TO_GROUP -> ModelVisibility.REGISTERED_ONLY
    AIVisibility.VISIBLE_TO_OWNER -> ModelVisibility.OWNER_ONLY
    AIVisibility.NONE -> null
}

private fun List<AIError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportModel() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun AIError.toTransportModel() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun AIState.toResult(): ResponseResult? = when (this) {
    AIState.RUNNING -> ResponseResult.SUCCESS
    AIState.FAILING -> ResponseResult.ERROR
    AIState.FINISHING -> ResponseResult.SUCCESS
    AIState.NONE -> null
}
