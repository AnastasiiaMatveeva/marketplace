package ru.otus.otuskotlin.aiassistant.app.spring.mock

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import ru.otus.otuskotlin.aiassistant.app.spring.config.ModelConfig
import ru.otus.otuskotlin.aiassistant.app.spring.controllers.ModelControllerV2Fine
import ru.otus.otuskotlin.aiassistant.api.v2.mappers.*
import ru.otus.otuskotlin.aiassistant.api.v2.models.*
import ru.otus.otuskotlin.aiassistant.biz.ModelProcessor
import AppContext
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(ModelControllerV2Fine::class, ModelConfig::class)
internal class ModelControllerV2EmptyTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: ModelProcessor

    @Test
    fun createModel() = testStubModel(
        "/v2/model/create",
        ModelCreateRequest(),
        AppContext().toTransportCreate()
    )

    @Test
    fun readModel() = testStubModel(
        "/v2/model/read",
        ModelReadRequest(),
        AppContext().toTransportRead()
    )

    @Test
    fun updateModel() = testStubModel(
        "/v2/model/update",
        ModelUpdateRequest(),
        AppContext().toTransportUpdate()
    )

    @Test
    fun deleteModel() = testStubModel(
        "/v2/model/delete",
        ModelDeleteRequest(),
        AppContext().toTransportDelete()
    )

    @Test
    fun searchModel() = testStubModel(
        "/v2/model/search",
        ModelSearchRequest(),
        AppContext().toTransportSearch()
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubModel(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
