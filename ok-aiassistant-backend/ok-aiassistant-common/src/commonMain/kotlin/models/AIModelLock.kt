package models

import kotlin.jvm.JvmInline

@JvmInline
value class AIModelLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = AIModelLock("")
    }
}
