package ru.otus.otuskotlin.aiassistant.biz
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import models.AIState
import AppContext

@Suppress("unused", "RedundantSuspendModifier")
class ModelProcessor(val corSettings: CorSettings) {
    suspend fun exec(ctx: AppContext) {
        ctx.modelResponse = ModelStub.get()
        ctx.modelsResponse = ModelStub.prepareSearchList("model search").toMutableList()
        ctx.state = AIState.RUNNING
    }
}
