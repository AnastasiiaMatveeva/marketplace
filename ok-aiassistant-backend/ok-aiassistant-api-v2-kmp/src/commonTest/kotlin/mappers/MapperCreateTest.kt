package ru.otus.otuskotlin.aiassistant.api.v2.mappers

import models.*
import kotlin.test.Test
import kotlin.test.assertEquals
import stubs.AIStubs
import ru.otus.otuskotlin.aiassistant.api.v2.models.*
import AppContext

class MapperCreateTest {
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
        )

        val context = AppContext()
        context.fromTransport(req)

        assertEquals(AIStubs.SUCCESS, context.stubCase)
        assertEquals(AIWorkMode.STUB, context.workMode)
        assertEquals("title", context.modelRequest.title)
        assertEquals("description", context.modelRequest.description)

        assertEquals(2, context.modelRequest.modelParams.size)

        assertEquals("param1", context.modelRequest.modelParams[0].name)
        assertEquals(AIParamType.DISCRETE, context.modelRequest.modelParams[0].paramType)
        assertEquals(1, context.modelRequest.modelParams[0].line)
        assertEquals(2, context.modelRequest.modelParams[0].position)
        assertEquals(0.0, context.modelRequest.modelParams[0].bounds.min)
        assertEquals(100.56, context.modelRequest.modelParams[0].bounds.max)

        assertEquals("param2", context.modelRequest.modelParams[1].name)
        assertEquals(AIParamType.CONTINUOUS, context.modelRequest.modelParams[1].paramType)
        assertEquals(3, context.modelRequest.modelParams[1].line)
        assertEquals(4, context.modelRequest.modelParams[1].position)
        assertEquals(";", context.modelRequest.modelParams[1].separator)
        assertEquals(-1.0, context.modelRequest.modelParams[1].bounds.min)
        assertEquals(2e5, context.modelRequest.modelParams[1].bounds.max)

        assertEquals("path/to/solver", context.modelRequest.solverPath)
        assertEquals("path/to/macro", context.modelRequest.scriptPath)
        assertEquals(AIVisibility.VISIBLE_PUBLIC, context.modelRequest.visibility)

    }

    @Test
    fun toTransport() {
        val context = AppContext(
            requestId = AIRequestId("1234"),
            command = AICommand.CREATE,
            modelResponse = AIModel(
                title = "title",
                description = "description",
                modelParams = listOf(
                    AIModelParam(
                        paramType = AIParamType.DISCRETE,
                        bounds = ParamBounds(
                            min = 0.0,
                            max = 100.0
                        ),
                        line = 1,
                        position = 2,
                        separator = ",",
                        name = "param1"
                    ),
                    AIModelParam(
                        paramType = AIParamType.CONTINUOUS,
                        bounds = ParamBounds(
                            min = -1.50,
                            max = 2.5e4,
                        ),
                        line = 3,
                        position = 4,
                        separator = ";",
                        name = "param2"
                    )
                ),
                scriptPath = "path/to/macro",
                solverPath = "path/to/solver",
                visibility = AIVisibility.VISIBLE_PUBLIC,
                lock = AIModelLock("not_lock")
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

        assertEquals("title", req.model?.title)
        assertEquals("description", req.model?.description)
        assertEquals(ModelVisibility.PUBLIC, req.model?.visibility)
        assertEquals(1, req.errors?.size)
        assertEquals("err", req.errors?.firstOrNull()?.code)
        assertEquals("request", req.errors?.firstOrNull()?.group)
        assertEquals("title", req.errors?.firstOrNull()?.field)
        assertEquals("wrong title", req.errors?.firstOrNull()?.message)

        assertEquals(ModelVisibility.PUBLIC, req.model?.visibility)

        assertEquals(2, req.model?.params?.size)

        assertEquals("param1", req.model?.params?.getOrNull(0)?.name)
        assertEquals(ModelParamType.DISCRETE, req.model?.params?.getOrNull(0)?.paramType)
        assertEquals(1, req.model?.params?.getOrNull(0)?.line)
        assertEquals(2, req.model?.params?.getOrNull(0)?.position)
        assertEquals(",", req.model?.params?.getOrNull(0)?.separator)
        assertEquals(0.0, req.model?.params?.getOrNull(0)?.bounds?.min)
        assertEquals(100.0, req.model?.params?.getOrNull(0)?.bounds?.max)

        assertEquals("param2", req.model?.params?.getOrNull(1)?.name)
        assertEquals(ModelParamType.CONTINUOUS, req.model?.params?.getOrNull(1)?.paramType)
        assertEquals(3, req.model?.params?.getOrNull(1)?.line)
        assertEquals(4, req.model?.params?.getOrNull(1)?.position)
        assertEquals(";", req.model?.params?.getOrNull(1)?.separator)
        assertEquals(-1.5, req.model?.params?.getOrNull(1)?.bounds?.min)
        assertEquals(2.5e4, req.model?.params?.getOrNull(1)?.bounds?.max)

        assertEquals("path/to/solver", req.model?.solverPath)
        assertEquals("path/to/macro", req.model?.scriptPath)
        assertEquals("not_lock", req.model?.lock)

    }
}
