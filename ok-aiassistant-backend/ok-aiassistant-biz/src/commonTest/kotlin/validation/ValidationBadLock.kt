package ru.otus.otuskotlin.aiassistant.biz.validation

import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import models.*

fun validationLockCorrect(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = AIModelId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.CONTINUOUS,
                    bounds = ParamBounds(
                        min = 100.0,
                        max = 200.0
                    ),
                    line = 1,
                    position = 2,
                    separator = ",",
                    name = "param1"
                )
            ),
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("123-234-abc-ABC"),
            features = arrayOf(1.0),
            ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(AIState.FAILING, ctx.state)
    assertEquals("123-234-abc-ABC", ctx.modelValidated.lock.asString())
}

fun validationLockTrim(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = AIModelId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.CONTINUOUS,
                    bounds = ParamBounds(
                        min = 100.0,
                        max = 200.0
                    ),
                    line = 1,
                    position = 2,
                    separator = ",",
                    name = "param1"
                )
            ),
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock(" \n\t 123-234-abc-ABC \n\t "),
            features = arrayOf(1.0),
            ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(AIState.FAILING, ctx.state)
    assertEquals("123-234-abc-ABC", ctx.modelValidated.lock.asString())
}

fun validationLockEmpty(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = AIModelId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.CONTINUOUS,
                    bounds = ParamBounds(
                        min = 100.0,
                        max = 200.0
                    ),
                    line = 1,
                    position = 2,
                    separator = ",",
                    name = "param1"
                )
            ),
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock(""),
            features = arrayOf(1.0),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("lock", ctx.errors.firstOrNull()?.field)
}

fun validationLockFormat(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = AIModelId("123-234-abc-ABC"),
            title = "abc",
            description = "abc",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.CONTINUOUS,
                    bounds = ParamBounds(
                        min = 100.0,
                        max = 200.0
                    ),
                    line = 1,
                    position = 2,
                    separator = ",",
                    name = "param1"
                )
            ),
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("!@#\$%^&*(),.{}"),
            features = arrayOf(1.0),
            ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("lock", ctx.errors.firstOrNull()?.field)
}
