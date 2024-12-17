import models.*
import ru.otus.otuskotlin.aiassistant.api.v1.models.*


fun AppContext.toTransportModel(): IResponse = when (val cmd = command) {
    Command.CREATE -> toTransportCreate()
    Command.READ -> toTransportRead()
    Command.UPDATE -> toTransportUpdate()
    Command.DELETE -> toTransportDelete()
    Command.SEARCH -> toTransportSearch()
    Command.NONE -> throw UnknownCommand(cmd)
}

fun AppContext.toTransportCreate() = ModelCreateResponse(
    result = state.toResult(),
    errors = modelErrors.toTransportErrors(),
    model = ModelResponse.toTransportModel()
)

fun AppContext.toTransportRead() = ModelReadResponse(
    result = state.toResult(),
    errors = modelErrors.toTransportErrors(),
    model = ModelResponse.toTransportModel()
)

fun AppContext.toTransportUpdate() = ModelUpdateResponse(
    result = state.toResult(),
    errors = modelErrors.toTransportErrors(),
    model = ModelResponse.toTransportModel()
)

fun AppContext.toTransportDelete() = ModelDeleteResponse(
    result = state.toResult(),
    errors = modelErrors.toTransportErrors(),
    model = ModelResponse.toTransportModel()
)

fun AppContext.toTransportSearch() = ModelSearchResponse(
    result = state.toResult(),
    errors = modelErrors.toTransportErrors(),
    models = ModelsResponse.toTransportModel()
)

fun List<Model>.toTransportModel(): List<ModelResponseObject>? = this
    .map { it.toTransportModel() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Model.toTransportModel(): ModelResponseObject = ModelResponseObject(
    id = id.takeIf { it != ModelId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
    visibility = visibility.toTransportModel(),
    permissions = permissionsClient.toTransportModel(),
)

private fun Set<ModelPermissionClient>.toTransportModel(): Set<ModelPermissions>? = this
    .map { it.toTransportModel() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun ModelPermissionClient.toTransportModel() = when (this) {
    ModelPermissionClient.READ -> ModelPermissions.READ
    ModelPermissionClient.UPDATE -> ModelPermissions.UPDATE
    ModelPermissionClient.MAKE_VISIBLE_OWNER -> ModelPermissions.MAKE_VISIBLE_OWN
    ModelPermissionClient.MAKE_VISIBLE_GROUP -> ModelPermissions.MAKE_VISIBLE_GROUP
    ModelPermissionClient.MAKE_VISIBLE_PUBLIC -> ModelPermissions.MAKE_VISIBLE_PUBLIC
    ModelPermissionClient.DELETE -> ModelPermissions.DELETE
}

private fun Visibility.toTransportModel(): ModelVisibility? = when (this) {
    Visibility.VISIBLE_PUBLIC -> ModelVisibility.PUBLIC
    Visibility.VISIBLE_TO_GROUP -> ModelVisibility.REGISTERED_ONLY
    Visibility.VISIBLE_TO_OWNER -> ModelVisibility.OWNER_ONLY
    Visibility.NONE -> null
}

private fun List<ModelError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportModel() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ModelError.toTransportModel() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun State.toResult(): ResponseResult? = when (this) {
    State.RUNNING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.FINISHING -> ResponseResult.SUCCESS
    State.NONE -> null
}
