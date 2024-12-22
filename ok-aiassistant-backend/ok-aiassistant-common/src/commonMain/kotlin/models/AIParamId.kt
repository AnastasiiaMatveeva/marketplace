package models

import kotlin.jvm.JvmInline

@JvmInline
value class AIParamId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = AIParamId("")
    }
}