import kotlinx.datetime.Instant
import models.*
import stubs.AIStubs

data class AppContext(
    var command: AICommand = AICommand.NONE,
    var state: AIState = AIState.NONE,
    val errors: MutableList<AIError> = mutableListOf(),

    var workMode: AIWorkMode = AIWorkMode.PROD,
    var stubCase: AIStubs = AIStubs.NONE,

    var requestId: AIRequestId = AIRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var modelRequest: AIModel = AIModel(),
    var modelFilterRequest: AIModelFilter = AIModelFilter(),

    var modelResponse: AIModel = AIModel(),
    var modelsResponse: MutableList<AIModel> = mutableListOf(),

    )
