package ru.otus.otuskotlin.aiassistant.stubs

import models.AIModel
import models.AIModelId
import ru.otus.otuskotlin.aiassistant.stubs.ModelStubTest.MODEL_STUB_TEST

object ModelStub {
    fun get(): AIModel = MODEL_STUB_TEST.copy()

    fun prepareResult(block: AIModel.() -> Unit): AIModel = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        model(MODEL_STUB_TEST,"1", filter),
        model(MODEL_STUB_TEST,"2", filter),
        model(MODEL_STUB_TEST,"3", filter),
        model(MODEL_STUB_TEST,"4", filter),
        model(MODEL_STUB_TEST,"5", filter),
        model(MODEL_STUB_TEST,"6", filter),
    )

    private fun model(base: AIModel, id: String, filter: String) = base.copy(
        id = AIModelId(id),
        title = "$filter $id",
        description = "description $filter $id",
    )

}
