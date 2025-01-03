package models
import kotlin.jvm.JvmInline

@JvmInline
value class AIRequestId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = AIRequestId("")
    }
}
