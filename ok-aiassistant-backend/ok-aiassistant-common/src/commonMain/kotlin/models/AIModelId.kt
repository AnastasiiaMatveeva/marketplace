package models

import kotlin.jvm.JvmInline

@JvmInline
value class AIModelId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = AIModelId("")
    }
}
