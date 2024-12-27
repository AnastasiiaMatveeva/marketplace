package ru.otus.otuskotlin.aiassistant.repo.postgresql

import com.benasher44.uuid.uuid4
import models.AIModel
import ru.otus.otuskotlin.aiassistant.repo.common.IRepoModelInitializable
import ru.otus.otuskotlin.aiassistant.repo.common.ModelRepoInitialized


object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "marketplace-pass"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<AIModel> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): IRepoModelInitializable = ModelRepoInitialized(
        repo = RepoModelSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}

