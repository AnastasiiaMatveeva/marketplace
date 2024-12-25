package ru.otus.otuskotlin.aiassistant.biz.validation

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import models.*
import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor


fun validationSearchStringCorrect(processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = AICommand.SEARCH,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelFilterRequest = AIModelFilter(searchString = "search"),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(AIState.FAILING, ctx.state)
    assertEquals("search", ctx.modelFilterValidated.searchString)
}

fun validationSearchStringTrim(processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = AICommand.SEARCH,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelFilterRequest = AIModelFilter(
            searchString = " \n\t search \n\t "
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(AIState.FAILING, ctx.state)
    assertEquals("search", ctx.modelFilterValidated.searchString)
}

fun validationSearchStringTooShort(processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = AICommand.SEARCH,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelFilterRequest = AIModelFilter(
            searchString = "se"
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("searchString", ctx.errors.firstOrNull()?.field)
}

fun validationSearchStringTooLong(processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = AICommand.SEARCH,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelFilterRequest = AIModelFilter(
            searchString = "search".repeat(20)
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("searchString", ctx.errors.firstOrNull()?.field)
}
