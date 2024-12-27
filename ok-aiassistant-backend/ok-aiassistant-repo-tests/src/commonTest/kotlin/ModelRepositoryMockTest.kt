package ru.otus.otuskotlin.aiassistant.repo.tests

import ru.otus.otuskotlin.aiassistant.stubs.ModelStub

import kotlinx.coroutines.test.runTest
import models.AIModel
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelResponseOk
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelsResponseOk
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelIdRequest
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelFilterRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


class ModelRepositoryMockTest {
    private val repo = ModelRepositoryMock(
        invokeCreateModel = { DbModelResponseOk(ModelStub.prepareResult { title = "create" }) },
        invokeReadModel = { DbModelResponseOk(ModelStub.prepareResult { title = "read" }) },
        invokeUpdateModel = { DbModelResponseOk(ModelStub.prepareResult { title = "update" }) },
        invokeDeleteModel = { DbModelResponseOk(ModelStub.prepareResult { title = "delete" }) },
        invokeSearchModel = { DbModelsResponseOk(listOf(ModelStub.prepareResult { title = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createModel(DbModelRequest(AIModel()))
        assertIs<DbModelResponseOk>(result)
        assertEquals("create", result.data.title)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readModel(DbModelIdRequest(AIModel()))
        assertIs<DbModelResponseOk>(result)
        assertEquals("read", result.data.title)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateModel(DbModelRequest(AIModel()))
        assertIs<DbModelResponseOk>(result)
        assertEquals("update", result.data.title)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteModel(DbModelIdRequest(AIModel()))
        assertIs<DbModelResponseOk>(result)
        assertEquals("delete", result.data.title)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchModel(DbModelFilterRequest())
        assertIs<DbModelsResponseOk>(result)
        assertEquals("search", result.data.first().title)
    }
}
