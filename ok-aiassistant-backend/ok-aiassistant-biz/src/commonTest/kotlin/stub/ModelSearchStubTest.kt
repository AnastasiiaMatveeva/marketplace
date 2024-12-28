package ru.otus.otuskotlin.aiassistant.biz.stubs

import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import kotlinx.coroutines.test.runTest
import models.*
import stubs.AIStubs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ModelSearchStubTest {

    private val processor = ModelProcessor()
    val filter = AIModelFilter(searchString = "EngineModeling")

    @Test
    fun search() = runTest {

        val ctx = AppContext(
            command = AICommand.SEARCH,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.SUCCESS,
            modelFilterRequest = filter,
        )
        processor.exec(ctx)
        assertTrue(ctx.modelsResponse.size > 1)
        val first_model = ctx.modelsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first_model.title.contains(filter.searchString))
        assertTrue(first_model.description.contains(filter.searchString))
        with (ModelStub.get()) {
            assertEquals(visibility, first_model.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = AppContext(
            command = AICommand.SEARCH,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_ID,
            modelFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badSearchString() = runTest {
        val ctx = AppContext(
            command = AICommand.SEARCH,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_SEARCH_STRING,
            modelFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("searchString", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = AppContext(
            command = AICommand.SEARCH,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.DB_ERROR,
            modelFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = AppContext(
            command = AICommand.SEARCH,
            state = AIState.NONE,
            workMode = AIWorkMode.STUB,
            stubCase = AIStubs.BAD_TITLE,
            modelFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(true, AIModel() == ctx.modelResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
