package ru.otus.otuskotlin.aiassistant.app.common

import kotlinx.datetime.Clock
import ru.otus.otuskotlin.aiassistant.api.log1.mapper.toLog
import ru.otus.otuskotlin.aiassistant.common.helpers.asAIError
import AppContext
import models.AIState
import kotlin.reflect.KClass

suspend inline fun <T> IAppSettings.controllerHelper(
    crossinline getRequest: suspend AppContext.() -> Unit,
    crossinline toResponse: suspend AppContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = AppContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = AIState.FAILING
        ctx.errors.add(e.asAIError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}
