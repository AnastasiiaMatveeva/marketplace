package models
import ru.otus.otuskotlin.aiassistant.logging.common.LogLevel

data class AIError(
    val code: String = "",
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val level: LogLevel = LogLevel.ERROR,
    val exception: Throwable? = null,
)

