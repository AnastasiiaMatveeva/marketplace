package ru.otus.otuskotlin.aiassistant.api.v2

import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.aiassistant.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    private val request: IRequest = ModelCreateRequest(
        debug = ModelDebug(
            mode = ModelRequestDebugMode.STUB,
            stub = ModelRequestDebugStubs.BAD_TITLE
        ),
        model = ModelCreateObject(
            title = "model title",
            description = "description",
            params = mutableListOf(
                ModelParam(
                    paramType = ModelParamType.DISCRETE,
                    bounds = Bounds(
                        min = 0.0,
                        max = 100.56
                    ),
                    line = 1,
                    position = 2,
                    separator = ",",
                    name = "param1"
                ),
                ModelParam(
                    paramType = ModelParamType.CONTINUOUS,
                    bounds = Bounds(
                        min = -1.0,
                        max = 2e5
                    ),
                    line = 3,
                    position = 4,
                    separator = ";",
                    name = "param2"
                )
            ),
            solverPath = "path/to/solver",
            scriptPath = "path/to/macro",
            visibility = ModelVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)

        println(json)

        assertContains(json, Regex("\"title\":\\s*\"model title\""))
        assertContains(json, Regex("\"mode\":\\s*\"stub\""))
        assertContains(json, Regex("\"stub\":\\s*\"badTitle\""))
        assertContains(json, Regex("\"requestType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString<IRequest>(json) as ModelCreateRequest

        assertEquals(request, obj)
    }
    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"Model": null}
        """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<ModelCreateRequest>(jsonString)

        assertEquals(null, obj.model)
    }
}
