package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import models.*
import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import kotlin.test.assertContains


private val stub = ModelStub.get()

fun validationDescriptionCorrect(command: AICommand, processor: ModelProcessor) = runBizTest {
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
    assertEquals("abc", ctx.modelValidated.description)
}

fun validationDescriptionTrim(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = stub.id,
            title = "abc",
            description = " \n\t abc \t\n ",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(AIState.FAILING, ctx.state)
    assertEquals("abc", ctx.modelValidated.description)
}

fun validationDescriptionEmpty(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = stub.id,
            title = "abc",
            description = "",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertContains(ctx.errors.firstOrNull()?.message ?: "", "description")
}

fun validationDescriptionSymbols(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = stub.id,
            title = "abc",
            description = "!@#\$%^&*(),.{}",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("123-234-abc-ABC"),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertContains(ctx.errors.firstOrNull()?.message ?: "", "description", )
}