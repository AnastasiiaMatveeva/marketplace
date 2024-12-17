import ru.otus.otuskotlin.aiassistant.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = ModelCreateRequest(
        debug = ModelDebug(
            mode = ModelRequestDebugMode.STUB,
            stub = ModelRequestDebugStubs.BAD_TITLE
        ),
        model = ModelCreateObject(
            title = "Model title",
            description = "Model description",
            visibility = ModelVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, Regex("\"title\":\\s*\"Model title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as ModelCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"Model": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, ModelCreateRequest::class.java)

        assertEquals(null, obj.model)
    }
}
