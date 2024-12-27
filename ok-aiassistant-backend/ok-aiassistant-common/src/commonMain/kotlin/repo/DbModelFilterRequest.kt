package ru.otus.otuskotlin.aiassistant.common.repo

import models.*
import ru.otus.otuskotlin.aiassistant.common.helpers.errorSystem
import ru.otus.otuskotlin.aiassistant.common.repo.exceptions.RepoConcurrencyException
import ru.otus.otuskotlin.aiassistant.common.repo.exceptions.RepoException

data class DbModelFilterRequest(
    val titleFilter: String = "",
    val ownerId: AIUserId = AIUserId.NONE,
)
