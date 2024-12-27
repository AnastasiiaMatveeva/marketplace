package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import models.AIState
import ru.otus.otuskotlin.aiassistant.common.repo.errorRepoConcurrency
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == AIState.RUNNING && modelValidated.lock != modelRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(modelRepoRead, modelValidated.lock).errors)
    }
}
