package ru.otus.otuskotlin.aiassistant.common.helpers

import AppContext
import models.AIError
import models.AIState
import ru.otus.otuskotlin.aiassistant.logging.common.LogLevel

fun Throwable.asError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = AIError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

inline fun AppContext.addError(vararg error: AIError) = errors.addAll(error)
inline fun AppContext.addErrors(error: Collection<AIError>) = errors.addAll(error)

inline fun AppContext.fail(error: AIError) {
    addError(error)
    state = AIState.FAILING
}

inline fun AppContext.fail(errors: Collection<AIError>) {
    addErrors(errors)
    state = AIState.FAILING
}

inline fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
    ) = AIError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
    )

inline fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = AIError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)

