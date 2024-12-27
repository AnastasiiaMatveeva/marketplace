package ru.otus.otuskotlin.aiassistant.repo.postgresql

import ru.otus.otuskotlin.aiassistant.repo.tests.*
import ru.otus.otuskotlin.aiassistant.common.repo.IRepoModel
import ru.otus.otuskotlin.aiassistant.repo.common.IRepoModelInitializable
import ru.otus.otuskotlin.aiassistant.repo.common.ModelRepoInitialized

import kotlin.test.AfterTest

private fun IRepoModel.clear() {
    val pgRepo = (this as ModelRepoInitialized).repo as RepoModelSql
    pgRepo.clear()
}

class RepoModelSQLCreateTest : RepoModelCreateTest() {
    override val repo: IRepoModelInitializable = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { uuidNew.asString() },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoModelSQLReadTest : RepoModelReadTest() {
    override val repo: IRepoModel = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoModelSQLUpdateTest : RepoModelUpdateTest() {
    override val repo: IRepoModel = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoModelSQLDeleteTest : RepoModelDeleteTest() {
    override val repo: IRepoModel = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoModelSQLSearchTest : RepoModelSearchTest() {
    override val repo: IRepoModel = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}
