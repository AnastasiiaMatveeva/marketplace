package ru.otus.otuskotlin.aiassistant.common.repo.exceptions

import models.AIModelId

open class RepoModelException(
    @Suppress("unused")
    val modelId: AIModelId,
    msg: String,
): RepoException(msg)
