package ru.otus.otuskotlin.aiassistant.cor.handlers

import ru.otus.otuskotlin.aiassistant.cor.CorDslMarker
import ru.otus.otuskotlin.aiassistant.cor.ICorExec
import ru.otus.otuskotlin.aiassistant.cor.ICorWorkerDsl

class CorWorker<T>(
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    private val blockHandle: suspend T.() -> Unit = {},
    blockExcept: suspend T.(Throwable) -> Unit = {},
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {
    override suspend fun handle(context: T) = blockHandle(context)
}

@CorDslMarker
class CorWorkerDsl<T> : CorExecDsl<T>(), ICorWorkerDsl<T> {
    private var blockHandle: suspend T.() -> Unit = {}
    override fun handle(function: suspend T.() -> Unit) {
        blockHandle = function
    }

    override fun build(): ICorExec<T> = CorWorker(
        title = title,
        description = description,
        blockOn = blockOn,
        blockHandle = blockHandle,
        blockExcept = blockExcept
    )

}
