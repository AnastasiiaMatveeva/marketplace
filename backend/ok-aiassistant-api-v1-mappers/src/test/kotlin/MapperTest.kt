import models.*
import org.junit.Test
import stubs.Stubs
import ru.otus.otuskotlin.aiassistant.api.v1.models.*
import kotlin.test.assertEquals

class MapperTest {
    @Test
    fun fromTransport() {
        val req = ModelCreateRequest(
            debug = ModelDebug(
                mode = ModelRequestDebugMode.STUB,
                stub = ModelRequestDebugStubs.SUCCESS,
            ),
            model = ModelCreateObject(
                title = "title",
                description = "description",
                params = listOf(
                    Param(
                        paramType = ModelParamType.CONTINUOUS,
                        line = 1,
                        position = 2,
                        separator = ",",
                        name = "param1"
                    ),
                    Param(
                        paramType = ModelParamType.DISCRETE,
                        line = 3,
                        position = 4,
                        separator = ";",
                        name = "param2"
                    )
                ),
                visibility = ModelVisibility.PUBLIC,
            ),
        )

        val context = AppContext()
        context.fromTransport(req)

        assertEquals(Stubs.SUCCESS, context.stubCase)
        assertEquals(WorkMode.STUB, context.workMode)
        assertEquals("title", context.ModelRequest.title)
        assertEquals(Visibility.VISIBLE_PUBLIC, context.ModelRequest.visibility)

        assertEquals(2, context.ModelRequest.modelParams.size)

        assertEquals("param1", context.ModelRequest.modelParams[0].name)
        assertEquals(ParamType.CONTINUOUS, context.ModelRequest.modelParams[0].paramType)
        assertEquals(1, context.ModelRequest.modelParams[0].line)
        assertEquals(2, context.ModelRequest.modelParams[0].position)
        assertEquals(",", context.ModelRequest.modelParams[0].separator)

        assertEquals("param2", context.ModelRequest.modelParams[1].name)
        assertEquals(ParamType.DISCRETE, context.ModelRequest.modelParams[1].paramType)
        assertEquals(3, context.ModelRequest.modelParams[1].line)
        assertEquals(4, context.ModelRequest.modelParams[1].position)
        assertEquals(";", context.ModelRequest.modelParams[1].separator)
    }

    @Test
    fun toTransport() {
        val context = AppContext(
            requestId = RequestId("1234"),
            command = Command.CREATE,
            ModelResponse = Model(
                title = "title",
                description = "description",
                visibility = Visibility.VISIBLE_PUBLIC,
            ),
            modelErrors = mutableListOf(
                ModelError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = State.RUNNING,
        )

        val req = context.toTransportModel() as ModelCreateResponse

        assertEquals("title", req.model?.title)
        assertEquals("description", req.model?.description)
        assertEquals(ModelVisibility.PUBLIC, req.model?.visibility)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)
    }
}
