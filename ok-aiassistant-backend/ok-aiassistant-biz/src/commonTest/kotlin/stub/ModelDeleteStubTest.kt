package ru.otus.otuskotlin.aiassistant.biz.stubs

import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import kotlinx.coroutines.test.runTest
import models.*
import stubs.AIStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import models.AIModelLock


class ModelDeleteStubTest {

    val processor = ModelProcessor()
    private val id = AIModelId("25")
    private val lock = AIModelLock("lock")


    @Test
    fun delete() = runTest {

        val ctx = AppContext(
            command = AICommand.DELETE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.SUCCESS,
            modelRequest = AIModel(
                id = id,
                lock = lock,
                ),
        )
        processor.exec(ctx)
        with (ModelStub.get()) {
            assertEquals(id, ctx.modelResponse.id)
            assertEquals(title, ctx.modelResponse.title)
            assertEquals(description, ctx.modelResponse.description)
            assertEquals(visibility, ctx.modelResponse.visibility)
        }
        assertEquals(id, ctx.modelResponse.id)
        assertEquals(lock, ctx.modelResponse.lock)
    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = AICommand.DELETE,
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
    fun databaseError() = runTest {
        val ctx = AppContext(
            command = AICommand.DELETE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.DB_ERROR,
            modelRequest = AIModel(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
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
            command = AICommand.DELETE,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_TITLE,
            modelRequest = AIModel(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
