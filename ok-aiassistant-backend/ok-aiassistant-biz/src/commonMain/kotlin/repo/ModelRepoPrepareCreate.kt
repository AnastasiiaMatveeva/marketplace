package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import models.AIState
import models.AIUserId
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == AIState.RUNNING }
    handle {
        modelRepoPrepare = modelValidated.deepCopy()
        // TODO будет реализовано в занятии по управлению пользвателями
        modelRepoPrepare.ownerId = AIUserId.NONE
    }
}
