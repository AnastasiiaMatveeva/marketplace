import models.*
import org.junit.Test
import stubs.AIStubs
import ru.otus.otuskotlin.aiassistant.api.v1.models.*
import kotlin.test.assertEquals
import ru.otus.otuskotlin.aiassistant.mappers.v1.fromTransport
import ru.otus.otuskotlin.aiassistant.mappers.v1.toTransportModel

class MapperPredictTest {
    @Test
    fun fromTransport() {
        val req = ModelPredictRequest(
            debug = ModelDebug(
                mode = ModelRequestDebugMode.STUB,
                stub = ModelRequestDebugStubs.SUCCESS,
            ),
            model = ModelPredictObject(
                id = "model_id",
                features = listOf(0.1, 0.2, 0.3, 0.4),
            ),
        )

        val context = AppContext()
        context.fromTransport(req)

        assertEquals(ModelRequestDebugMode.STUB.name, context.workMode.name)
        assertEquals(ModelRequestDebugStubs.SUCCESS.name, context.stubCase.name)
        assertEquals(4, context.modelRequest.features.size)
        assertEquals(0.1, context.modelRequest.features.get(0))
        assertEquals(0.2, context.modelRequest.features.get(1))
        assertEquals(0.3, context.modelRequest.features.get(2))
        assertEquals(0.4, context.modelRequest.features.get(3))
    }

    @Test
    fun toTransport() {
        val context = AppContext(
            requestId = AIRequestId("1234"),
            command = AICommand.CREATE,
            modelResponse = AIModel(
                id = AIModelId("model_id"),
                features = arrayOf(0.1, 0.2, 0.3, 0.4),
                results = arrayOf(0.5, 0.6, 0.7, 0.8),
            ),
            errors = mutableListOf(
                AIError(
                    code = "err",
                    group = "request",
                    field = "title",
                    message = "wrong title",
                )
            ),
            state = AIState.RUNNING,

            )

        val req = context.toTransportModel() as ModelCreateResponse

        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)

        assertEquals(4, req.model?.features?.size)
        assertEquals(0.1, req.model?.features?.get(0))
        assertEquals(0.2, req.model?.features?.get(1))
        assertEquals(0.3, req.model?.features?.get(2))
        assertEquals(0.4, req.model?.features?.get(3))

        assertEquals(4, req.model?.results?.size)
        assertEquals(0.5, req.model?.results?.get(0))
        assertEquals(0.6, req.model?.results?.get(1))
        assertEquals(0.7, req.model?.results?.get(2))
        assertEquals(0.8, req.model?.results?.get(3))
    }
}
