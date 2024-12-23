package ru.otus.otuskotlin.aiassistant.app.rabbit.controllers

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import ru.otus.otuskotlin.aiassistant.api.v2.apiV2RequestDeserialize
import ru.otus.otuskotlin.aiassistant.api.v2.apiV2ResponseSerialize
import ru.otus.otuskotlin.aiassistant.api.v2.mappers.fromTransport
import ru.otus.otuskotlin.aiassistant.api.v2.mappers.toTransportModel
import ru.otus.otuskotlin.aiassistant.api.v2.models.IRequest
import ru.otus.otuskotlin.aiassistant.app.common.controllerHelper
import ru.otus.otuskotlin.aiassistant.app.rabbit.config.AppSettings
import AppContext
import ru.otus.otuskotlin.aiassistant.common.helpers.asError
import models.AIState

class RabbitDirectControllerV2(
    private val appSettings: AppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV2,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {

    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV2RequestDeserialize<IRequest>(String(message.body))
                fromTransport(req)
            },
            {
                val res = toTransportModel()
                apiV2ResponseSerialize(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
                }
            },
            RabbitDirectControllerV2::class,
            "rabbitmq-v2-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = AppContext()
        e.printStackTrace()
        context.state = AIState.FAILING
        context.errors.add(e.asError())
        val response = context.toTransportModel()
        apiV2ResponseSerialize(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
        }
    }
}
