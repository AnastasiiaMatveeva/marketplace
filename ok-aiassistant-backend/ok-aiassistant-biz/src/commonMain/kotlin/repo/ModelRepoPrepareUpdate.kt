package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import models.AIState
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == AIState.RUNNING }
    handle {
        modelRepoPrepare = modelRepoRead.deepCopy().apply {
            this.title = modelValidated.title
            description = modelValidated.description
            visibility = modelValidated.visibility
            lock = modelValidated.lock
        }
    }
}
