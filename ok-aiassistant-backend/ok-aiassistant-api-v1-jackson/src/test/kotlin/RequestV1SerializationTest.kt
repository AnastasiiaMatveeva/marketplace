import ru.otus.otuskotlin.aiassistant.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import ru.otus.otuskotlin.aiassistant.api.v1.apiV1Mapper


class RequestV1SerializationTest {
    private val request = ModelCreateRequest(
        debug = ModelDebug(
            mode = ModelRequestDebugMode.STUB,
            stub = ModelRequestDebugStubs.BAD_TITLE
        ),
        model = ModelCreateObject(
            title = "Model title",
            description = "Model description",
            scriptPath = "path/to/macro",
            solverPath = "path/to/solver",
            params = listOf(
                ModelParam(
                    line = 0,
                    position = 2,
                    separator = "=",
                    name = "Temperature",
                    bounds = Bounds(0.0, 1.0),
                    paramType = ModelParamType.DISCRETE
                ),
            ),
            visibility = ModelVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        println(json)
        assertContains(json, Regex("\"title\":\\s*\"Model title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
        assertContains(json, Regex("\"description\":\\s*\"Model description\""))
        assertContains(json, Regex("\"script_path\":\\s*\"path/to/macro\""))
        assertContains(json, Regex("\"solver_path\":\\s*\"path/to/solver\""))
        assertContains(json, Regex("\"name\":\\s*\"Temperature\""))
        assertContains(json, Regex("\"line\":\\s*0"))
        assertContains(json, Regex("\"position\":\\s*2"))
        assertContains(json, Regex("\"separator\":\\s*\"=\""))
        assertContains(json, Regex("\"bounds\":\\s*\\{\"min\":\\s*0.0,\\s*\"max\":\\s*1.0\\}"))
        assertContains(json, Regex("\"param_type\":\\s*\"discrete\""))
        assertContains(json, Regex("\"visibility\":\\s*\"public\""))    }

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
