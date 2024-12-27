package ru.otus.otuskotlin.aiassistant.common.repo

import models.*
import ru.otus.otuskotlin.aiassistant.common.helpers.errorSystem
import ru.otus.otuskotlin.aiassistant.common.repo.exceptions.RepoConcurrencyException
import ru.otus.otuskotlin.aiassistant.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorNotFound(id: AIModelId) = DbModelResponseErr(
    AIError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbModelResponseErr(
    AIError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

fun errorRepoConcurrency(
    oldModel: AIModel,
    expectedLock: AIModelLock,
    exception: Exception = RepoConcurrencyException(
        id = oldModel.id,
        expectedLock = expectedLock,
        actualLock = oldModel.lock,
    ),
) = DbModelResponseErrWithData(
    model = oldModel,
    err = AIError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldModel.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorEmptyLock(id: AIModelId) = DbModelResponseErr(
    AIError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Model ${id.asString()} is empty that is not admitted"
    )
)

fun errorDb(e: RepoException) = DbModelResponseErr(
    errorSystem(
        violationCode = "db-error",
        e = e
    )
)
