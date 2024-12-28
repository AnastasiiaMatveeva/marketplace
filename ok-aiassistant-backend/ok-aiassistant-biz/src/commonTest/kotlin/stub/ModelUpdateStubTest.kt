package ru.otus.otuskotlin.aiassistant.biz.stubs

import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import kotlinx.coroutines.test.runTest
import models.*
import stubs.AIStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelUpdateStubTest {

    val processor = ModelProcessor()
    val id = AIModelId("32")
    val lock = AIModelLock("lock")
    val title = "name 32"
    val description = "desc 32"
    val scriptPath = "путь/к/макросу"
    val solverPath = "путь/к/солверу"
    val minOfbound = 250.0
    val position = 5

    @Test
    fun update() = runTest {

        val ctx = AppContext(
            command = AICommand.UPDATE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.SUCCESS,
            modelRequest = AIModel(
                id = id,
                lock = lock,
                title = title,
                description = description,
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
        println(scriptPath)
        assertEquals(description, ctx.modelResponse.description)
        assertEquals(scriptPath, ctx.modelResponse.scriptPath)
        assertEquals(solverPath, ctx.modelResponse.solverPath)
        assertEquals(minOfbound, ctx.modelResponse.modelParams.firstOrNull()?.bounds?.min)
        assertEquals(position, ctx.modelResponse.modelParams.firstOrNull()?.position)
    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = AICommand.UPDATE,
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
    fun badTitle() = runTest {
        val ctx = AppContext(
            command = AICommand.UPDATE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_TITLE,
            modelRequest = AIModel(
                id = id,
                title = "",
                description = description,
                visibility = AIVisibility.VISIBLE_TO_OWNER,
            ),
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badDescription() = runTest { // Раскомментировал и исправил
        val ctx = AppContext(
            command = AICommand.UPDATE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_DESCRIPTION,
            modelRequest = AIModel(
                id = id,
                title = "",
                description = description,
                visibility = AIVisibility.VISIBLE_TO_OWNER,
            ),
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badParamPosition() = runTest { // Раскомментировал и исправил
        val ctx = AppContext(
            command = AICommand.UPDATE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_PARAM_POSITION,
            modelRequest = AIModel(
                id = id,
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
                        bounds = ParamBounds(250.0, 380.0),
                    )
                ),
            )
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("position", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badParamType() = runTest { // Раскомментировал и исправил
        val ctx = AppContext(
            command = AICommand.UPDATE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_PARAM_TYPE,
            modelRequest = AIModel(
                id = id,
                title = title,
                description = description,
                scriptPath = "путь/к/макросу",
                solverPath = "путь/к/солверу",
                modelParams = mutableListOf(
                    AIModelParam(
                        paramType = AIParamType.NONE,
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
        assertEquals("paramType", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badParamBounds() = runTest {
        val ctx = AppContext(
            command = AICommand.UPDATE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_PARAM_BOUNDS,
            modelRequest = AIModel(
                id = id,
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
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("paramBounds", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
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
            ),
        )
        processor.exec(ctx)
        assertEquals(true, AIModel().equals(ctx.modelResponse))
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
