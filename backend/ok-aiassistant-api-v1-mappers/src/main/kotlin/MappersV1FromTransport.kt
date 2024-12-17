import exceptions.UnknownRequestClass
import models.*
import models.ParamType
import stubs.Stubs
import ru.otus.otuskotlin.aiassistant.api.v1.models.*


fun AppContext.fromTransport(request: IRequest) = when (request) {
    is ModelCreateRequest -> fromTransport(request)
    is ModelReadRequest -> fromTransport(request)
    is ModelUpdateRequest -> fromTransport(request)
    is ModelDeleteRequest -> fromTransport(request)
    is ModelSearchRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

private fun String?.toModelId() = this?.let { ModelId(it) } ?: ModelId.NONE
private fun String?.toModelWithId() = Model(id = this.toModelId())
private fun String?.toModelLock() = this?.let { ModelLock(it) } ?: ModelLock.NONE

private fun ModelDebug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    ModelRequestDebugMode.PROD -> WorkMode.PROD
    ModelRequestDebugMode.TEST -> WorkMode.TEST
    ModelRequestDebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun ModelDebug?.transportToStubCase(): Stubs = when (this?.stub) {
    ModelRequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    ModelRequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    ModelRequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    ModelRequestDebugStubs.BAD_TITLE -> Stubs.BAD_TITLE
    ModelRequestDebugStubs.BAD_DESCRIPTION -> Stubs.BAD_DESCRIPTION
    ModelRequestDebugStubs.BAD_VISIBILITY -> Stubs.BAD_VISIBILITY
    ModelRequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    ModelRequestDebugStubs.BAD_SEARCH_STRING -> Stubs.BAD_SEARCH_STRING
    null -> Stubs.NONE
}

fun AppContext.fromTransport(request: ModelCreateRequest) {
    command = Command.CREATE
    ModelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: ModelReadRequest) {
    command = Command.READ
    ModelRequest = request.model.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ModelReadObject?.toInternal(): Model = if (this != null) {
    Model(id = id.toModelId())
} else {
    Model()
}


fun AppContext.fromTransport(request: ModelUpdateRequest) {
    command = Command.UPDATE
    ModelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun AppContext.fromTransport(request: ModelDeleteRequest) {
    command = Command.DELETE
    ModelRequest = request.model.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ModelDeleteObject?.toInternal(): Model = if (this != null) {
    Model(
        id = id.toModelId(),
        lock = lock.toModelLock(),
    )
} else {
    Model()
}

fun AppContext.fromTransport(request: ModelSearchRequest) {
    command = Command.SEARCH
    ModelFilterRequest = request.modelFilter.toInternal()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ModelSearchFilter?.toInternal(): ModelFilter = ModelFilter(
    searchString = this?.searchString ?: ""
)

private fun ModelCreateObject.toInternal(): Model = Model(
    title = this.title ?: "",
    description = this.description ?: "",
    modelParams = this.params?.map { it.toInternal() } ?: emptyList(),
    visibility = this.visibility.fromTransport(),
)

private fun ModelUpdateObject.toInternal(): Model = Model(
    id = this.id.toModelId(),
    title = this.title ?: "",
    description = this.description ?: "",
    modelParams = this.params?.map { it.toInternal() } ?: emptyList(),
    visibility = this.visibility.fromTransport(),
    lock = lock.toModelLock(),
)

private fun ModelVisibility?.fromTransport(): Visibility = when (this) {
    ModelVisibility.PUBLIC -> Visibility.VISIBLE_PUBLIC
    ModelVisibility.OWNER_ONLY -> Visibility.VISIBLE_TO_OWNER
    ModelVisibility.REGISTERED_ONLY -> Visibility.VISIBLE_TO_GROUP
    null -> Visibility.NONE
}

private fun ModelParamType?.fromTransport(): ParamType = when (this) {
    ModelParamType.CONTINUOUS -> ParamType.CONTINUOUS
    ModelParamType.DISCRETE -> ParamType.DISCRETE
    null -> ParamType.NONE
}

private fun Param?.toInternal(): ModelParam = if (this != null) {
    ModelParam(
        paramType = this.paramType.fromTransport(),
        line = this.line ?: 0,
        position = this.position ?: 0,
        separator = this.separator ?: "",
        name = this.name ?: ""
    )
} else {
    ModelParam()
}



