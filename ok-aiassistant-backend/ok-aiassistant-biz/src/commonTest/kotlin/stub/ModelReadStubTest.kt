package ru.otus.otuskotlin.aiassistant.biz.stubs

import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import kotlinx.coroutines.test.runTest
import models.*
import stubs.AIStubs
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelReadStubTest {

    val processor = ModelProcessor()
    private val id = AIModelId("25")

    @Test
    fun read() = runTest {

        val ctx = AppContext(
            command = AICommand.READ,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.SUCCESS,
            modelRequest = AIModel(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (ModelStub.get()) {
            assertEquals(id, ctx.modelResponse.id)
            assertEquals(title, ctx.modelResponse.title)
            assertEquals(description, ctx.modelResponse.description)
            assertEquals(visibility, ctx.modelResponse.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = AICommand.READ,
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
            command = AICommand.READ,
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
    fun badNoCase() = runTest {
        val ctx = AppContext(
            command = AICommand.READ,
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
