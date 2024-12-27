package ru.otus.otuskotlin.aiassistant.common.repo

import models.AIModel
import models.AIError

sealed interface IDbModelResponse: IDbResponse<AIModel>

data class DbModelResponseOk(
    val data: AIModel
): IDbModelResponse

data class DbModelResponseErr(
    val errors: List<AIError> = emptyList()
): IDbModelResponse {
    constructor(err: AIError): this(listOf(err))
}

data class DbModelResponseErrWithData(
    val data: AIModel,
    val errors: List<AIError> = emptyList()
): IDbModelResponse {
    constructor(model: AIModel, err: AIError): this(model, listOf(err))
}
