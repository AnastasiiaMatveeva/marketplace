package ru.otus.otuskotlin.aiassistant.biz.validation

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import models.*
import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor


fun validationParamsCorrect(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
    command = command,
    state = AIState.NONE,
    workMode = AIWorkMode.TEST,
    modelRequest = AIModel(
        id = AIModelId("123-234-abc-ABC"),
        title = "title",
        description = "description",
        modelParams = mutableListOf(
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
        lock = AIModelLock("notLock")
        )
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(AIState.FAILING, ctx.state)
    assertEquals(ctx.modelRequest.modelParams, ctx.modelValidated.modelParams)
}

fun validationParamsBadPosition(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = AIModelId("123"),
            title = "title",
            description = "description",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.DISCRETE,
                    bounds = ParamBounds(
                        min = 0.0,
                        max = 100.0
                    ),
                    line = 1,
                    position = -2,
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
                    position = 0,
                    separator = ";",
                    name = "param2"
                )
            ),
            scriptPath = "path/to/macro",
            solverPath = "path/to/solver",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("notLock")
        )
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("position", ctx.errors.firstOrNull()?.field)
}


fun validationParamsBadBoundsMinMax(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = AIModelId("123"),
            title = "title",
            description = "description",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.DISCRETE,
                    bounds = ParamBounds(
                        min = 200.0,
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
                    position = 1,
                    separator = ";",
                    name = "param2"
                )
            ),
            scriptPath = "path/to/macro",
            solverPath = "path/to/solver",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("notLock")
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("bounds", ctx.errors.firstOrNull()?.field)
}

fun validationParamsBadBoundsExist(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = AIModelId("123"),
            title = "title",
            description = "description",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.DISCRETE,
                    bounds = ParamBounds(
                        min = 100.0,
                        max = 200.0
                    ),
                    line = 1,
                    position = 2,
                    separator = ",",
                    name = "param1"
                ),
                AIModelParam(
                    paramType = AIParamType.CONTINUOUS,
                    bounds = ParamBounds(
                        min = Double.NaN,
                        max = 2.5e4,
                    ),
                    line = 3,
                    position = 1,
                    separator = ";",
                    name = "param2"
                )
            ),
            scriptPath = "path/to/macro",
            solverPath = "path/to/solver",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("notLock")
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("bounds", ctx.errors.firstOrNull()?.field)
}

fun validationParamsBadType(command: AICommand, processor: ModelProcessor) = runBizTest {
    val ctx = AppContext(
        command = command,
        state = AIState.NONE,
        workMode = AIWorkMode.TEST,
        modelRequest = AIModel(
            id = AIModelId("123-234-abc-ABC"),
            title = "title",
            description = "description",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.NONE,
                    bounds = ParamBounds(
                        min = 100.0,
                        max = 200.0
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
                    position = 1,
                    separator = ";",
                    name = "param2"
                )
            ),
            scriptPath = "path/to/macro",
            solverPath = "path/to/solver",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("notLock")
        )
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("paramType", ctx.errors.firstOrNull()?.field)
}

