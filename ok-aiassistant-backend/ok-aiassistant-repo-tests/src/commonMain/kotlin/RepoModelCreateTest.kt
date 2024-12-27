package ru.otus.otuskotlin.aiassistant.repo.tests

import models.*
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseOk
import ru.otus.otuskotlin.aiassistant.repo.common.IRepoModelInitializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import runRepoTest

abstract class RepoModelCreateTest {
    abstract val repo: IRepoModelInitializable
    protected open val uuidNew = AIModelId("10000000-0000-0000-0000-000000000001")

    private val createObj = AIModel(
        title = "create object",
        description = "create object description",
        ownerId = AIUserId("owner-123"),
        lock = AIModelLock("lock"),
        visibility = AIVisibility.VISIBLE_TO_OWNER,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createModel(DbModelRequest(createObj))
        val expected = createObj
        println(result)
        println(expected)

        assertIs<DbModelResponseOk>(result)
        assertEquals(uuidNew, result.data.id)
        assertEquals(expected.title, result.data.title)
        assertEquals(expected.description, result.data.description)
    }

    companion object : BaseInitModels("create") {
        override val initObjects: List<AIModel> = emptyList()
    }
}
