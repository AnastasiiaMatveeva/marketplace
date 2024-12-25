import kotlinx.datetime.Instant
import models.*
import stubs.AIStubs
import ru.otus.otuskotlin.aiassistant.common.ws.IWsSession
import ru.otus.otuskotlin.aiassistant.common.CorSettings

data class AppContext(
    var command: AICommand = AICommand.NONE,
    var state: AIState = AIState.NONE,
    val errors: MutableList<AIError> = mutableListOf(),

    var corSettings: CorSettings = CorSettings(),
    var workMode: AIWorkMode = AIWorkMode.PROD,
    var stubCase: AIStubs = AIStubs.NONE,
    var wsSession: IWsSession = IWsSession.NONE,

    var requestId: AIRequestId = AIRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var modelRequest: AIModel = AIModel(),
    var modelFilterRequest: AIModelFilter = AIModelFilter(),

    var modelValidating: AIModel = AIModel(),
    var modelFilterValidating: AIModelFilter = AIModelFilter(),

    var modelValidated: AIModel = AIModel(),
    var modelFilterValidated: AIModelFilter = AIModelFilter(),

    var modelResponse: AIModel = AIModel(),
    var modelsResponse: MutableList<AIModel> = mutableListOf(),
    ) {
}
