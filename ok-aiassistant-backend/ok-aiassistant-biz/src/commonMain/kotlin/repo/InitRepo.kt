package ru.otus.otuskotlin.aiassistant.biz.repo

import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.fail
import ru.otus.otuskotlin.aiassistant.common.helpers.errorSystem
import models.AIWorkMode
import ru.otus.otuskotlin.aiassistant.biz.exceptions.AIModelDbNotConfiguredException
import ru.otus.otuskotlin.aiassistant.cor.ICorChainDsl
import ru.otus.otuskotlin.aiassistant.cor.worker
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel


fun ICorChainDsl<AppContext>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        modelRepo = when {
            workMode == AIWorkMode.TEST -> corSettings.repoTest
            workMode == AIWorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        println("workMode $workMode")
        if (workMode != AIWorkMode.STUB && modelRepo == IRepoModel.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = AIModelDbNotConfiguredException(workMode)
            )
        )
    }
}
