package ru.otus.otuskotlin.aiassistant.repo.inmemory

import models.ParamBounds

data class ParamBoundsEntity(
    val min: Double? = null,
    val max: Double? = null,
) {
    constructor(bounds: ParamBounds) : this(
        min = bounds.min,
        max = bounds.max
    )

    fun toInternal() = ParamBounds(
        min = min ?: Double.NaN,
        max = max ?: Double.NaN
    )
}