package ru.otus.otuskotlin.aiassistant.app.rabbit.controllers

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.otus.otuskotlin.aiassistant.api.v1.apiV1Mapper
import ru.otus.otuskotlin.aiassistant.api.v1.models.IRequest
import ru.otus.otuskotlin.aiassistant.app.common.controllerHelper
import ru.otus.otuskotlin.aiassistant.app.rabbit.config.AppSettings
import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.asError
import models.AIState
import ru.otus.otuskotlin.aiassistant.mappers.v1.fromTransport
import ru.otus.otuskotlin.aiassistant.mappers.v1.toTransportModel

// наследник RabbitProcessorBase, увязывает транспортную и бизнес-части
class RabbitDirectControllerV1(
    private val appSettings: AppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV1,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV1Mapper.readValue(message.body, IRequest::class.java)
                fromTransport(req)
            },
            {
                val res = toTransportModel()
                apiV1Mapper.writeValueAsBytes(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
                }
            },
            this@RabbitDirectControllerV1::class,
            "rabbitmq-v1-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = AppContext()
        e.printStackTrace()
        context.state = AIState.FAILING
        context.errors.add(e.asError())
        val response = context.toTransportModel()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
        }
    }
}
