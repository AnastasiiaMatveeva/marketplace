package ru.otus.otuskotlin.aiassistant.biz.repo

import kotlin.random.Random
import AppContext
import models.AIState
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker

fun ICorChainDsl<AppContext>.repoPrepareTrain(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == AIState.RUNNING }
    handle {
        modelRepoDone = modelRepoRead.deepCopy()
        modelRepoPrepare = modelRepoRead.deepCopy()
        modelRepoPrepare.status = "completed"
        modelRepoPrepare.duration = Random.nextDouble(1.0, 11.0)

        }
    }

