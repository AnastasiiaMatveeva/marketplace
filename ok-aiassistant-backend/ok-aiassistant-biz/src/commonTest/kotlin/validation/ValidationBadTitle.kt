package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import models.*
import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import kotlin.test.assertContains


private val stub = ModelStub.get()

fun validationTitleCorrect(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = stub.id,
            title = "abc",
            description = "abc",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(AIState.FAILING, ctx.state)
    assertEquals("abc", ctx.modelValidated.title)
}

fun validationTitleTrim(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = stub.id,
            title = " \n\t abc \t\n ",
            description = "abc",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(AIState.FAILING, ctx.state)
    assertEquals("abc", ctx.modelValidated.title)
}

fun validationTitleEmpty(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = stub.id,
            title = "",
            description = "abc",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("title",  ctx.errors.firstOrNull()?.field?: "")
}

fun validationTitleSymbols(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = stub.id,
            title = "!@#$%^&*(),.{}",
            description = "abc",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertContains(ctx.errors.firstOrNull()?.message ?: "", "title")
}