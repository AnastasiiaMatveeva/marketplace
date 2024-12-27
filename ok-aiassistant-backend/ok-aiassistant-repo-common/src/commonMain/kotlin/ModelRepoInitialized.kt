package ru.otus.otuskotlin.aiassistant.repo.common

import models.AIModel

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class ModelRepoInitialized(
    val repo: IRepoModelInitializable,
    initObjects: Collection<AIModel> = emptyList(),
) : IRepoModelInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<AIModel> = save(initObjects).toList()
}
