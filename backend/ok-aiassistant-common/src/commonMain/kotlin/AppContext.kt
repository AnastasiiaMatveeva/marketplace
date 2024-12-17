import kotlinx.datetime.Instant
import models.*
import stubs.Stubs

data class AppContext(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val modelErrors: MutableList<ModelError> = mutableListOf(),

    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var ModelRequest: Model = Model(),
    var ModelFilterRequest: ModelFilter = ModelFilter(),

    var ModelResponse: Model = Model(),
    var ModelsResponse: MutableList<Model> = mutableListOf(),

    )
