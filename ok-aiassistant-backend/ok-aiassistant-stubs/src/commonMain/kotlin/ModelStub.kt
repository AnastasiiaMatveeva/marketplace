package ru.otus.otuskotlin.aiassistant.stubs

import models.AIModel
import models.AIModelId
import ru.otus.otuskotlin.aiassistant.stubs.ModelStubTest.MODEL_STUB_TEST

object ModelStub {
    fun get(): AIModel = MODEL_STUB_TEST.copy()

    fun prepareResult(block: AIModel.() -> Unit): AIModel = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        model(MODEL_STUB_TEST,"d-666-01", filter),
        model(MODEL_STUB_TEST,"d-666-02", filter),
        model(MODEL_STUB_TEST,"d-666-03", filter),
        model(MODEL_STUB_TEST,"d-666-04", filter),
        model(MODEL_STUB_TEST,"d-666-05", filter),
        model(MODEL_STUB_TEST,"d-666-06", filter),
    )

    private fun model(base: AIModel, id: String, filter: String) = base.copy(
        id = AIModelId(id),
        title = "$filter $id",
        description = "desc $filter $id",
    )

}
