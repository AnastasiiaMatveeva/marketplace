package ru.otus.otuskotlin.aiassistant.biz.stubs

import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.AIStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelPredictStubTest {

    val processor = ModelProcessor()
    val id = AIModelId("25")
    private val lock = AIModelLock("lock")
    val title = "task"
    val description = "description task"
    val scriptPath = "путь/к/макросу"
    val solverPath = "путь/к/солверу"
    val minOfbound = 250.0
    val position = 5
    val features = arrayOf(1.0, 2.0, 3.0, 4.0, 5.0)


    @Test
    fun predict() = runTest {

        val ctx = AppContext(
            command = AICommand.PREDICT,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.SUCCESS,
            modelRequest = AIModel(
                id = id,
                lock = lock,
                title = title,
                description = description,
                features = features,
                scriptPath = "путь/к/макросу",
                solverPath = "путь/к/солверу",
                modelParams = mutableListOf(
                    AIModelParam(
                        paramType = AIParamType.CONTINUOUS,
                        line = 1,
                        position = 5,
                        separator = "=",
                        name = "Температура",
                        bounds = ParamBounds(250.0, 380.0),
                    )
                ),
                visibility = AIVisibility.VISIBLE_TO_OWNER,
            )
        )
        processor.exec(ctx)
        assertEquals(lock, ctx.modelResponse.lock)
        assertEquals(description, ctx.modelResponse.description)
        assertEquals(scriptPath, ctx.modelResponse.scriptPath)
        assertEquals(solverPath, ctx.modelResponse.solverPath)
        assertEquals(features, ctx.modelResponse.features)
        assertEquals(minOfbound, ctx.modelResponse.modelParams.firstOrNull()?.bounds?.min)
        assertEquals(position, ctx.modelResponse.modelParams.firstOrNull()?.position)
    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = AICommand.PREDICT,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_ID,
            modelRequest = AIModel(),
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badFeatures() = runTest {
        val ctx = AppContext(
            command = AICommand.PREDICT,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_FEATURES,
            modelRequest = AIModel(
                id = id,
                title = title,
                description = description,
                scriptPath = "путь/к/макросу",
                solverPath = "путь/к/солверу",
                features = arrayOf(),
                modelParams = mutableListOf(
                    AIModelParam(
                        paramType = AIParamType.CONTINUOUS,
                        line = 1,
                        position = -1,
                        separator = "=",
                        name = "Температура",
                        bounds = ParamBounds(250.0, 380.0),
                    )
                ),
            )
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("features", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = AppContext(
            command = AICommand.UPDATE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.DB_ERROR,
            modelRequest = AIModel(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(true, AIModel().equals(ctx.modelResponse))
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badLock() = runTest {
        val ctx = AppContext(
            command = AICommand.UPDATE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_LOCK,
            modelRequest = AIModel(
                id = id,
                lock = AIModelLock.NONE,
                title = title,
                description = description,
                scriptPath = "путь/к/макросу",
                solverPath = "путь/к/солверу",
                modelParams = mutableListOf(
                    AIModelParam(
                        paramType = AIParamType.CONTINUOUS,
                        line = 1,
                        position = -1,
                        separator = "=",
                        name = "Температура",
                        bounds = ParamBounds(380.0, 250.0),
                    )
                ),
            )
        )
        processor.exec(ctx)
        assertEquals(true, AIModel().equals(ctx.modelResponse))
        assertEquals("lock",       ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = AppContext(
            command = AICommand.UPDATE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_SEARCH_STRING,
            modelRequest = AIModel(
                id = id,
                title = title,
                description = description,
                features = arrayOf(1.0, 2.0, 3.0, 4.0, 5.0),
                ),
        )
        processor.exec(ctx)
        assertEquals(true, AIModel().equals(ctx.modelResponse))
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
