package ru.otus.otuskotlin.aiassistant.repo.inmemory

import models.AIModelParam
import models.AIParamId
import models.AIParamType
import ru.otus.otuskotlin.aiassistant.repo.inmemory.ParamBoundsEntity

data class ModelParamEntity(
    val id: String? = null,
    val paramType: String? = null,
    val bounds: ParamBoundsEntity = ParamBoundsEntity(),
    val line: Int = 0,
    val position: Int = 0,
    val separator: String? = null,
    val name: String? = null,
) {
    constructor(param: AIModelParam) : this(
        id = param.id.asString().takeIf { it.isNotBlank() },
        paramType = param.paramType.name.takeIf { it.isNotBlank() },
        bounds = ParamBoundsEntity(param.bounds),
        line = param.line,
        position = param.position,
        separator = param.separator.takeIf { it.isNotBlank() },
        name = param.name.takeIf { it.isNotBlank() }
    )

    fun toInternal() = AIModelParam(
        id = id?.let { AIParamId(it) } ?: AIParamId.NONE,
        paramType = paramType?.let { AIParamType.valueOf(it) } ?: AIParamType.NONE,
        bounds = bounds.toInternal(),
        line = line,
        position = position,
        separator = separator ?: "",
        name = name ?: ""
    )
}
