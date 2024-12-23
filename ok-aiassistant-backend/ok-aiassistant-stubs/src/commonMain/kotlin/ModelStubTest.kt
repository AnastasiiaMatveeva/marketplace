package ru.otus.otuskotlin.aiassistant.stubs

import models.*

object ModelStubTest {
    val MODEL_STUB_TEST: AIModel
        get() = AIModel(
            id = AIModelId("25"),
            ownerId = AIUserId("Unknown"),
            permissionsClient = mutableSetOf(
                AIModelPermissionClient.READ,
                AIModelPermissionClient.UPDATE,
                AIModelPermissionClient.DELETE,
                AIModelPermissionClient.MAKE_VISIBLE_PUBLIC,
                AIModelPermissionClient.MAKE_VISIBLE_GROUP,
                AIModelPermissionClient.MAKE_VISIBLE_OWNER,
            ),
            title = "",
            scriptPath = "путь/к/макросу",
            solverPath = "путь/к/солверу",
            modelParams = mutableListOf(
                AIModelParam(
                    paramType = AIParamType.CONTINUOUS,
                    line = 1,
                    position = 5,
                    separator = "=",
                    name = "Температура",
                    bounds = ParamBounds(250.0, 380.0),
                )
            ),
            visibility = AIVisibility.VISIBLE_TO_OWNER,
        )
}
