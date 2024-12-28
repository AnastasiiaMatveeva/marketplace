package ru.otus.otuskotlin.aiassistant.repo.postgresql

import kotlinx.serialization.Serializable

@Serializable
data class JsonParams(
    val params: List<JsonParam>
)

@Serializable
data class JsonParam(
    val line: Int,
    val position: Int,
    val separator: String,
    val name: String,
    val bounds: JsonBounds,
    val type: String,
)

@Serializable
data class JsonBounds(
    val min: Double,
    val max: Double
)
