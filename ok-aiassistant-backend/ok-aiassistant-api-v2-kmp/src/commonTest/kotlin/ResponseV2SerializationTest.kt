package ru.otus.otuskotlin.aiassistant.api.v2

import kotlinx.serialization.encodeToString
import ru.otus.otuskotlin.aiassistant.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse = ModelCreateResponse(
        model = ModelResponseObject(
            title = "title",
            description = "description",
            params = listOf(
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
//        val json = apiV2Mapper.encodeToString(AdRequestSerializer1, request)
//        val json = apiV2Mapper.encodeToString(RequestSerializers.create, request)
        val json = apiV2Mapper.encodeToString(response)

        assertContains(json, Regex("\"title\":\\s*\"ad title\""))
        assertContains(json, Regex("\"responseType\":\\s*\"create\""))
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString<IResponse>(json) as ModelCreateResponse

        assertEquals(response, obj)
    }
}
