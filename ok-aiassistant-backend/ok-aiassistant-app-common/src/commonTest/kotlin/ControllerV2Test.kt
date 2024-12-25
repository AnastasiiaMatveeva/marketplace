package ru.otus.otuskotlin.aiassistant.app.common

import kotlinx.coroutines.test.runTest
import ru.otus.otuskotlin.aiassistant.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.aiassistant.api.v2.mappers.toTransportModel
import ru.otus.otuskotlin.aiassistant.api.v2.models.*
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {

    private val request = ModelCreateRequest(
        model = ModelCreateObject(
            title = "title",
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
        ),
        debug = ModelDebug(mode = ModelRequestDebugMode.STUB, stub = ModelRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IAppSettings = object : IAppSettings {
        override val corSettings: CorSettings = CorSettings()
        override val processor: ModelProcessor = ModelProcessor(corSettings)
    }

    private suspend fun createAdSpring(request: ModelCreateRequest): ModelCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportModel() as ModelCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createAdKtor(appSettings: IAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<ModelCreateRequest>()) },
            { toTransportModel() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createAdSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createAdKtor(appSettings) }
        val res = testApp.res as ModelCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
