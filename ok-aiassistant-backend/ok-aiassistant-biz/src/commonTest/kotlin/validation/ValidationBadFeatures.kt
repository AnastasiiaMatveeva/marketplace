package ru.otus.otuskotlin.aiassistant.biz.validation

import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import models.*
import AppContext
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor


fun validationFeaturesCorrect(command: AICommand, processor: ModelProcessor) = runBizTest {
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
            features = arrayOf(1.0, 2.0),
            scriptPath = "path/to/macro",
            solverPath = "path/to/solver",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("notLock")
        )
    )

    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(AIState.FAILING, ctx.state)
    assertEquals(ctx.modelRequest.features, ctx.modelValidated.features)
}

fun validationFeaturesEmpty(command: AICommand, processor: ModelProcessor) = runBizTest {
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
            features = arrayOf(),
            lock = AIModelLock("123-234-abc-ABC"),
            )
    )

    processor.exec(ctx)
    assertEquals(2, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("features", ctx.errors.firstOrNull()?.field)
}

fun validationFeaturesSize(command: AICommand, processor: ModelProcessor) = runBizTest {
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
            features = arrayOf(1.0, 2.0, 3.0),
            scriptPath = "path/to/macro",
            solverPath = "path/to/solver",
            visibility = AIVisibility.VISIBLE_PUBLIC,
            lock = AIModelLock("notLock")
        )
    )

    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(AIState.FAILING, ctx.state)
    assertEquals("features", ctx.errors.firstOrNull()?.field)
}


