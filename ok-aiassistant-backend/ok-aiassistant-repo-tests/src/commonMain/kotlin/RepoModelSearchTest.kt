package ru.otus.otuskotlin.aiassistant.repo.tests

import models.AIModel
import models.AIUserId
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelsResponseOk
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel
import ru.otus.otuskotlin.aiassistant.common.repo.DbModelFilterRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import runRepoTest

abstract class RepoModelSearchTest {
    abstract val repo: IRepoModel

    protected open val initializedObjects: List<AIModel> = initObjects

    @Test
    fun searchOwner() = runRepoTest {
        val result = repo.searchModel(DbModelFilterRequest(ownerId = searchOwnerId))
        println("RESULT1 $result")
        assertIs<DbModelsResponseOk>(result)
        val expected = listOf(initializedObjects[1], initializedObjects[3]).sortedBy { it.id.asString() }
        assertEquals(expected, result.data.sortedBy { it.id.asString() })
    }

    companion object: BaseInitModels("search") {

        val searchOwnerId = AIUserId("owner-124")
        override val initObjects: List<AIModel> = listOf(
            createInitTestModel("model1"),
            createInitTestModel("model2", ownerId = searchOwnerId),
            createInitTestModel("model3"),
            createInitTestModel("model4", ownerId = searchOwnerId),
            createInitTestModel("model5"),
        )
    }
}
