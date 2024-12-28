package ru.otus.otuskotlin.aiassistant.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.aiassistant.api.v1.models.*
import AppContext
import models.*
import ru.otus.otuskotlin.aiassistant.mappers.v1.*
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import kotlin.test.Test

internal abstract class ModelRepoBaseTest {
    protected abstract var webClient: WebTestClient
    private val debug = ModelDebug(mode = ModelRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000001"

    @Test
    open fun createModel() = testRepoModel(
        "create",
        ModelCreateRequest(
            model = ModelStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(ModelStub.prepareResult {
            id = AIModelId(uuidNew)
            lock = AIModelLock(uuidNew)
            features = emptyArray()
            ownerId = AIUserId("")

        })
            .toTransportCreate()
            .copy(responseType = "create")
    )

    @Test
    open fun readModel() = testRepoModel(
        "read",
        ModelReadRequest(
            model = ModelStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(ModelStub.get())
            .toTransportRead()
            .copy(responseType = "read")
    )

    @Test
    open fun updateModel() = testRepoModel(
        "update",
        ModelUpdateRequest(
            model = ModelStub.prepareResult { title = "add" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(ModelStub.prepareResult { title = "add"; lock = AIModelLock(uuidNew) })
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    open fun deleteModel() = testRepoModel(
        "delete",
        ModelDeleteRequest(
            model = ModelStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(ModelStub.get())
            .toTransportDelete()
            .copy(responseType = "delete")
    )

    @Test
    open fun searchModel() = testRepoModel(
        "search",
        ModelSearchRequest(
            modelFilter = ModelSearchFilter(searchString= "xxx"),
            debug = debug,
        ),
        AppContext(
            state = AIState.RUNNING,
            modelsResponse = ModelStub.prepareSearchList("xxx")
                .onEach { it.permissionsClient.clear() }
                .sortedBy { it.id.asString() }
                .toMutableList()
        )
            .toTransportSearch().copy(responseType = "search")
    )

    @Test
    open fun trainModel() = testRepoModel(
        "train",
        ModelTrainRequest(
            model = ModelStub.prepareResult {lock = AIModelLock(uuidNew) }.toTransportTrain(),
            debug = debug,
        ),
        prepareCtx(ModelStub.prepareResult {lock = AIModelLock(uuidNew) })
            .toTransportTrain().copy(responseType = "train")

    )

    @Test
    open fun predictModel() = testRepoModel(
        "predict",
        ModelPredictRequest(
            model = ModelStub.prepareResult {
                features = arrayOf(0.1, 0.2)
                status = "ok"
            }.toTransportPredict(),
            debug = debug,
        ),
        prepareCtx(ModelStub.prepareResult {
            features = arrayOf(0.1, 0.2)
            lock = AIModelLock(uuidNew)
            results = arrayOf(0.4, 0.5)
        })
            .toTransportPredict().copy(responseType = "predict")
    )


    private fun prepareCtx(model: AIModel) = AppContext(
        state = AIState.RUNNING,
//        modelResponse = model
        modelResponse = model.apply {
            // Пока не реализована эта функциональность
            permissionsClient.clear()
        },
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoModel(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v1/model/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is ModelSearchResponse -> it.copy(models = it.models?.sortedBy { it.id })
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
