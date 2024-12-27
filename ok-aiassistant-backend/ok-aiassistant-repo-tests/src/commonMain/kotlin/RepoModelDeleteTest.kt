package ru.otus.otuskotlin.aiassistant.repo.tests

import models.AIModel
import models.AIModelId
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelIdRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseOk
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErr
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErrWithData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import runRepoTest


abstract class RepoModelDeleteTest {
    abstract val repo: IRepoModel
    protected open val deleteSucc = initObjects[0]
    protected open val deleteConc = initObjects[1]
    protected open val notFoundId = AIModelId("model-repo-delete-notFound")

    @Test
    fun deleteSuccess() = runRepoTest {
        val lockOld = deleteSucc.lock
        val result = repo.deleteModel(DbModelIdRequest(deleteSucc.id, lock = lockOld))
        assertIs<DbModelResponseOk>(result)
        assertEquals(deleteSucc.title, result.data.title)
        assertEquals(deleteSucc.description, result.data.description)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.readModel(DbModelIdRequest(notFoundId, lock = lockOld))

        assertIs<DbModelResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertNotNull(error)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteModel(DbModelIdRequest(deleteConc.id, lock = lockBad))

        assertIs<DbModelResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitModels("delete") {
        override val initObjects: List<AIModel> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
    }
}
