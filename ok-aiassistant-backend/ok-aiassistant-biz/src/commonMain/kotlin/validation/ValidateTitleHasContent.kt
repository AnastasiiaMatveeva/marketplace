package ru.otus.otuskotlin.aiassistant.biz.validation

import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.common.helpers.errorValidation

fun ICorChainDsl<AppContext>.validateTitleHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в заголовке.
        Отказываем в публикации заголовков, в которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { modelValidating.title.isNotEmpty() && ! modelValidating.title.contains(regExp) }
    handle {
        fail(
            errorValidation(
            field = "title",
            violationCode = "noContent",
            description = "field must contain letters"
        )
        )
    }
}
