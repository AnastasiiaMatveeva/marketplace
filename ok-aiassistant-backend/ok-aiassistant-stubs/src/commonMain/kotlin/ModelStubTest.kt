package ru.otus.otuskotlin.aiassistant.stubs

import models.*

object ModelStubTest {
    val MODEL_STUB_TEST: AIModel
        get() = AIModel(
            id = AIModelId("25"),
            ownerId = AIUserId("Unknown"),
            title = "task",
            description = "description task",
            scriptPath = "путь/к/макросу",
            solverPath = "путь/к/солверу",
            status = "ok",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.CONTINUOUS,
                    line = 1,
                    position = 5,
                    separator = "=",
                    name = "Температура",
                    bounds = ParamBounds(250.0, 380.0),
                ),
                AIModelParam(
                    paramType = AIParamType.CONTINUOUS,
                    line = 1,
                    position = 5,
                    separator = "=",
                    name = "Температура",
                    bounds = ParamBounds(1.0, 5.0),
                    )
            ),
            features = arrayOf(1.0, 2.0),
            visibility = AIVisibility.VISIBLE_TO_OWNER,
            lock = AIModelLock("123-234-abc-ABC"),
            )
}
