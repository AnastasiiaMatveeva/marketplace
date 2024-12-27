package ru.otus.otuskotlin.aiassistant.common.repo.exceptions

import models.AIModelId

class RepoEmptyLockException(id: AIModelId): RepoModelException(
    id,
    "Lock is empty in DB"
)
