package ru.otus.otuskotlin.aiassistant.common.repo.exceptions

import models.AIModelId
import models.AIModelLock

class RepoConcurrencyValidation(id: AIModelId, expectedLock: AIModelLock, actualLock: AIModelLock?): RepoModelException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
