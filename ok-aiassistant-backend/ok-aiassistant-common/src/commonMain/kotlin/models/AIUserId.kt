package models

import kotlin.jvm.JvmInline

@JvmInline
value class AIUserId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = AIUserId("")
    }
}
