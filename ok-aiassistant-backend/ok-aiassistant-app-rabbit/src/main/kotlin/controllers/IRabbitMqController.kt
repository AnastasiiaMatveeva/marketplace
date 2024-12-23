package ru.otus.otuskotlin.aiassistant.app.rabbit.controllers

import ru.otus.otuskotlin.aiassistant.app.rabbit.config.RabbitExchangeConfiguration

interface IRabbitMqController {
    val exchangeConfig: RabbitExchangeConfiguration
    suspend fun process()
    fun close()
}

