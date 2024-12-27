package ru.otus.otuskotlin.aiassistant.repo.common

import models.AIModel
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel

interface IRepoModelInitializable: IRepoModel {
    fun save(models: Collection<AIModel>): Collection<AIModel>
}
