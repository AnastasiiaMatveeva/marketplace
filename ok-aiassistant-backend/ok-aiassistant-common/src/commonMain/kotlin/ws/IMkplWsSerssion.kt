package ru.otus.otuskotlin.aiassistant.common.ws

interface IWsSession {
    suspend fun <T> send(obj: T)
    companion object {
        val NONE = object : IWsSession {
            override suspend fun <T> send(obj: T) {

            }
        }
    }
}
