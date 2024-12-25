package ru.otus.otuskotlin.aiassistant.biz.stubs

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import models.AIError
import stubs.AIStubs
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.stubValidationBadSearchString(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации поискового запроса
    """.trimIndent()

    on { stubCase == AIStubs.BAD_SEARCH_STRING && state == AIState.RUNNING }
    handle {
        fail(
            AIError(
                group = "validation",
                code = "validation-searchString",
                field = "searchString",
                message = "Wrong searchString field"
            )
        )
    }
}
