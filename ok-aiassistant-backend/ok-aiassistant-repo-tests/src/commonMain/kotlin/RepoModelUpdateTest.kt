package ru.otus.otuskotlin.aiassistant.repo.tests

import models.*
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseOk
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErr
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseErrWithData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import runRepoTest

abstract class RepoModelUpdateTest {
    abstract val repo: IRepoModel
    protected open val updateSucc = initObjects[0]
    protected open val updateConc = initObjects[1]
    protected val updateIdNotFound = AIModelId("model-repo-update-not-found")
    protected val lockBad = AIModelLock("20000000-0000-0000-0000-000000000009")
    protected val lockNew = AIModelLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        AIModel(
            id = updateSucc.id,
            title = "update object",
            description = "update object description",
            ownerId = AIUserId("owner-123"),
            lock = initObjects.first().lock,
            visibility = AIVisibility.VISIBLE_PUBLIC
        )
    }
    private val reqUpdateNotFound = AIModel(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = AIUserId("owner-123"),
        lock = initObjects.first().lock,
        visibility = AIVisibility.VISIBLE_PUBLIC
    )

    private val reqUpdateConc by lazy {
        AIModel(
            id = updateConc.id,
            title = "update object not found",
            description = "update object not found description",
            ownerId = AIUserId("owner-123"),
            lock = lockBad,
            visibility = AIVisibility.VISIBLE_PUBLIC
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateModel(DbModelRequest(reqUpdateSucc))
        assertIs<DbModelResponseOk>(result)
        assertEquals(reqUpdateSucc.id, result.data.id)
        assertEquals(reqUpdateSucc.title, result.data.title)
        assertEquals(reqUpdateSucc.description, result.data.description)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateModel(DbModelRequest(reqUpdateNotFound))
        assertIs<DbModelResponseErr>(result)
        val error = result.errors.find { it.code == "repo-not-found" }
        assertEquals("id", error?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateModel(DbModelRequest(reqUpdateConc))
        assertIs<DbModelResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertEquals("lock", error?.field)
        assertEquals(updateConc, result.data)
    }

    companion object : BaseInitModels("update") {
        override val initObjects: List<AIModel> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
