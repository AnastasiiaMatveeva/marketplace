package ru.otus.otuskotlin.aiassistant.repo.inmemory

import models.*
import ru.otus.otuskotlin.aiassistant.repo.inmemory.ModelParamEntity

data class ModelEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val modelParams: List<ModelParamEntity> = emptyList(),
    val solverPath: String? = null,
    val scriptPath: String? = null,
    val features: List<Double> = emptyList(),
    val results: List<Double> = emptyList(),
    val ownerId: String? = null,
    val visibility: String? = null,
    val permissionsClient: List<String> = emptyList(),
    val lock: String? = null,
) {
    constructor(model: AIModel) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        modelParams = model.modelParams.map { ModelParamEntity(it) },
        solverPath = model.solverPath.takeIf { it.isNotBlank() },
        scriptPath = model.scriptPath.takeIf { it.isNotBlank() },
        features = model.features.toList(),
        results = model.results.toList(),
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        visibility = model.visibility.name.takeIf { it.isNotBlank() },
        permissionsClient = model.permissionsClient.map { it.name },
        lock = model.lock.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = AIModel(
        id = id?.let { AIModelId(it) } ?: AIModelId.NONE,
        title = title ?: "",
        description = description ?: "",
        modelParams = modelParams.map { it.toInternal() }.toMutableList(),
        solverPath = solverPath ?: "",
        scriptPath = scriptPath ?: "",
        features = features.toTypedArray(),
        results = results.toTypedArray(),
        ownerId = ownerId?.let { AIUserId(it) } ?: AIUserId.NONE,
        visibility = visibility?.let { AIVisibility.valueOf(it) } ?: AIVisibility.NONE,
        permissionsClient = permissionsClient.map { AIModelPermissionClient.valueOf(it) }.toMutableSet(),
        lock = lock?.let { AIModelLock(it) } ?: AIModelLock.NONE,
    )
}





