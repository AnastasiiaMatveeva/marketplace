package ru.otus.otuskotlin.aiassistant.repo.tests

import models.AIModel
import models.AIModelId
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelIdRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseOk
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErr
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel
import kotlin.test.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import runRepoTest

abstract class RepoModelReadTest {
    abstract val repo: IRepoModel
    protected open val readSucc = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readModel(DbModelIdRequest(readSucc.id))
        assertIs<DbModelResponseOk>(result)
        assertEquals(readSucc, result.data)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.readModel(DbModelIdRequest(notFoundId))

        assertIs<DbModelResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitModels("delete") {
        override val initObjects: List<AIModel> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = AIModelId("model-repo-read-notFound")

    }
}
