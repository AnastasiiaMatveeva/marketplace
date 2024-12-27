package ru.otus.otuskotlin.aiassistant.common.repo

import models.AIModel
import models.AIError

sealed interface IDbModelsResponse: IDbResponse<List<AIModel>>

data class DbModelsResponseOk(
    val data: List<AIModel>
): IDbModelsResponse

@Suppress("unused")
data class DbModelsResponseErr(
    val errors: List<AIError> = emptyList()
): IDbModelsResponse {
    constructor(err: AIError): this(listOf(err))
}
