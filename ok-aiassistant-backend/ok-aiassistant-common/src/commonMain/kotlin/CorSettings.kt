package ru.otus.otuskotlin.aiassistant.common

import ru.otus.otuskotlin.aiassistant.common.ws.IWsSessionRepo
import ru.otus.otuskotlin.aiassistant.logging.common.LoggerProvider
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val wsSessions: IWsSessionRepo = IWsSessionRepo.NONE,
    val repoStub: IRepoModel = IRepoModel.NONE,
    val repoTest: IRepoModel = IRepoModel.NONE,
    val repoProd: IRepoModel = IRepoModel.NONE,

    ) {
    companion object {
        val NONE = CorSettings()
    }
}
