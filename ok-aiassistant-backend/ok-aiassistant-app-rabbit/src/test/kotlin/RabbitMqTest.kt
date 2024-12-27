package ru.otus.otuskotlin.aiassistant.app.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.RabbitMQContainer
import ru.otus.otuskotlin.aiassistant.api.v1.apiV1Mapper
import ru.otus.otuskotlin.aiassistant.api.v1.models.ModelCreateObject
import ru.otus.otuskotlin.aiassistant.api.v1.models.ModelCreateRequest
import ru.otus.otuskotlin.aiassistant.api.v1.models.ModelCreateResponse
import ru.otus.otuskotlin.aiassistant.api.v1.models.ModelDebug
import ru.otus.otuskotlin.aiassistant.api.v1.models.ModelRequestDebugMode
import ru.otus.otuskotlin.aiassistant.api.v1.models.ModelRequestDebugStubs
import ru.otus.otuskotlin.aiassistant.api.v1.models.ModelParamType
import ru.otus.otuskotlin.aiassistant.api.v1.models.ModelParam
import ru.otus.otuskotlin.aiassistant.api.v1.models.Bounds
import ru.otus.otuskotlin.aiassistant.api.v1.models.ModelVisibility
import ru.otus.otuskotlin.aiassistant.app.rabbit.config.AppSettings
import ru.otus.otuskotlin.aiassistant.app.rabbit.config.RabbitConfig
import ru.otus.otuskotlin.aiassistant.app.rabbit.config.RabbitExchangeConfiguration
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import ru.otus.otuskotlin.aiassistant.api.v2.apiV2RequestSerialize
import ru.otus.otuskotlin.aiassistant.api.v2.apiV2ResponseDeserialize
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelCreateObject as ModelCreateObjectV2
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelCreateRequest as ModelCreateRequestV2
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelCreateResponse as ModelCreateResponseV2
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelDebug as ModelDebugV2
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelRequestDebugMode as ModelRequestDebugModeV2
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelRequestDebugStubs as ModelRequestDebugStubsV2
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelParamType as ModelParamTypeV2
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelParam as ModelParamV2
import ru.otus.otuskotlin.aiassistant.api.v2.models.Bounds as BoundsV2
import ru.otus.otuskotlin.aiassistant.api.v2.models.ModelVisibility as ModelVisibilityV2


//  тесты с использованием testcontainers
internal class RabbitMqTest {

    companion object {
        const val exchange = "test-exchange"
        const val exchangeType = "direct"
        const val RMQ_PORT = 5672

        private val container = run {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
            RabbitMQContainer("rabbitmq:latest").apply {
//                withExposedPorts(5672, 15672) // Для 3-management
                withExposedPorts(RMQ_PORT)
            }
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
//            println("CONTAINER PORT (15672): ${container.getMappedPort(15672)}")
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = AppSettings(
        rabbit = RabbitConfig(
            port = container.getMappedPort(RMQ_PORT)
        ),
//        corSettings = MkplCorSettings(loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }),
        controllersConfigV1 = RabbitExchangeConfiguration(
            keyIn = "in-v1",
            keyOut = "out-v1",
            exchange = exchange,
            queue = "v1-queue",
            consumerTag = "v1-consumer-test",
            exchangeType = exchangeType
        ),
        controllersConfigV2 = RabbitExchangeConfiguration(
            keyIn = "in-v2",
            keyOut = "out-v2",
            exchange = exchange,
            queue = "v2-queue",
            consumerTag = "v2-consumer-test",
            exchangeType = exchangeType
        ),
    )
    private val app = RabbitApp(appSettings = appSettings)

    @BeforeTest
    fun tearUp() {
        app.start()
    }

    @AfterTest
    fun tearDown() {
        println("Test is being stopped")
        app.close()
    }

    @Test
    fun modelCreateTestV1() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV1) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV1Mapper.writeValueAsBytes(modelAICreateV1))

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV1Mapper.readValue(responseJson, ModelCreateResponse::class.java)
                val expected = ModelStub.get()

                assertEquals(expected.title, response.model?.title)
                assertEquals(expected.description, response.model?.description)
                assertEquals(expected.scriptPath, response.model?.scriptPath)
                assertEquals(expected.solverPath, response.model?.solverPath)
                assertEquals(expected.modelParams.firstOrNull()?.line, response.model?.params?.firstOrNull()?.line)
                assertEquals(expected.modelParams.firstOrNull()?.position, response.model?.params?.firstOrNull()?.position)
                assertEquals(expected.modelParams.firstOrNull()?.separator, response.model?.params?.firstOrNull()?.separator)
                assertEquals(expected.modelParams.firstOrNull()?.name, response.model?.params?.firstOrNull()?.name)
                assertEquals(expected.modelParams.firstOrNull()?.bounds?.max, response.model?.params?.firstOrNull()?.bounds?.max)
                assertEquals(expected.modelParams.firstOrNull()?.paramType?.name, response.model?.params?.firstOrNull()?.paramType?.name)
            }
        }
    }

    @Test
    fun modelCreateTestV2() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV2) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(exchange, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, exchange, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(exchange, keyIn, null, apiV2RequestSerialize(modelAICreateV2).toByteArray())

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV2ResponseDeserialize<ModelCreateResponseV2>(responseJson)
                val expected = ModelStub.get()
                assertEquals(expected.title, response.model?.title)
                assertEquals(expected.description, response.model?.description)
                assertEquals(expected.scriptPath, response.model?.scriptPath)
                assertEquals(expected.solverPath, response.model?.solverPath)
                assertEquals(expected.modelParams.firstOrNull()?.line, response.model?.params?.firstOrNull()?.line)
                assertEquals(expected.modelParams.firstOrNull()?.position, response.model?.params?.firstOrNull()?.position)
                assertEquals(expected.modelParams.firstOrNull()?.separator, response.model?.params?.firstOrNull()?.separator)
                assertEquals(expected.modelParams.firstOrNull()?.name, response.model?.params?.firstOrNull()?.name)
                assertEquals(expected.modelParams.firstOrNull()?.bounds?.max, response.model?.params?.firstOrNull()?.bounds?.max)
                assertEquals(expected.modelParams.firstOrNull()?.paramType?.name, response.model?.params?.firstOrNull()?.paramType?.name)
            }
        }
    }

    private val modelAICreateV1 = with(ModelStub.get()) {
        ModelCreateRequest(
            model = ModelCreateObject(
                title = "task",
                scriptPath = "путь/к/макросу",
                solverPath = "путь/к/солверу",
                params = mutableListOf(
                    ModelParam(
                        paramType = ModelParamType.DISCRETE,
                        line = 1,
                        position = 2,
                        separator = "=",
                        name = "Скорость",
                        bounds = Bounds(100.0, 200.0),
                    ),
                ),
                visibility = ModelVisibility.PUBLIC,
            ),
            debug = ModelDebug(
                mode = ModelRequestDebugMode.STUB,
                stub = ModelRequestDebugStubs.SUCCESS
            )
        )
    }

    private val modelAICreateV2 = with(ModelStub.get()) {
        ModelCreateRequestV2(
            model = ModelCreateObjectV2(
                title = "task",
                scriptPath = "путь/к/макросу",
                solverPath = "путь/к/солверу",
                params = listOf(
                    ModelParamV2(
                        paramType = ModelParamTypeV2.DISCRETE,
                        line = 1,
                        position = 2,
                        separator = "=",
                        name = "Скорость",
                        bounds = BoundsV2(100.0, 200.0),
                    ),
                ),
                visibility = ModelVisibilityV2.PUBLIC,
            ),
            debug = ModelDebugV2(
                mode = ModelRequestDebugModeV2.STUB,
                stub = ModelRequestDebugStubsV2.SUCCESS
            )
        )
    }
}
