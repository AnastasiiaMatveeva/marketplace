package ru.otus.otuskotlin.aiassistant.app.spring.repo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import ru.otus.otuskotlin.aiassistant.app.spring.config.ModelConfig
import ru.otus.otuskotlin.aiassistant.app.spring.controllers.ModelControllerV1Fine
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelFilterRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelIdRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelRequest
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel
import ru.otus.otuskotlin.aiassistant.repo.common.ModelRepoInitialized
import ru.otus.otuskotlin.aiassistant.repo.inmemory.ModelRepoInMemory
import ru.otus.otuskotlin.aiassistant.stubs.ModelStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(
    ModelControllerV1Fine::class, ModelConfig::class,
    properties = ["spring.main.allow-bean-definition-overriding=true"]
)
@Import(RepoInMemoryConfig::class)
internal class ModelRepoInMemoryTest : ModelRepoBaseTest() {

    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testTestRepo: IRepoModel

    @BeforeEach
    fun tearUp() {
        val slotModel = slot<DbModelRequest>()
        val slotId = slot<DbModelIdRequest>()
        val slotFl = slot<DbModelFilterRequest>()
        val repo = ModelRepoInitialized(
            repo = ModelRepoInMemory(randomUuid = { uuidNew }),
            initObjects = ModelStub.prepareSearchList("xxx") + ModelStub.get()
        )
        println(repo)
        coEvery { testTestRepo.createModel(capture(slotModel)) } coAnswers { repo.createModel(slotModel.captured) }
        coEvery { testTestRepo.readModel(capture(slotId)) } coAnswers { repo.readModel(slotId.captured) }
        coEvery { testTestRepo.updateModel(capture(slotModel)) } coAnswers { repo.updateModel(slotModel.captured) }
        coEvery { testTestRepo.deleteModel(capture(slotId)) } coAnswers { repo.deleteModel(slotId.captured) }
        coEvery { testTestRepo.searchModel(capture(slotFl)) } coAnswers { repo.searchModel(slotFl.captured) }

    }

    @Test
    override fun createModel() = super.createModel()

    @Test
    override fun readModel() = super.readModel()

    @Test
    override fun updateModel() = super.updateModel()

    @Test
    override fun deleteModel() = super.deleteModel()

    @Test
    override fun searchModel() = super.searchModel()

//    @Test
//    override fun trainModel() = super.trainModel()
//
//    @Test
//    override fun predictModel() = super.predictModel()
}
