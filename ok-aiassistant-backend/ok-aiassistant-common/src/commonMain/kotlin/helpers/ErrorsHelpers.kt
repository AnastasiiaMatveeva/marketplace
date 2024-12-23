package ru.otus.otuskotlin.aiassistant.common.helpers

import models.AIError

fun Throwable.asAIError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = AIError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)
