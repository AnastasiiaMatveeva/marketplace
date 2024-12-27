import ru.otus.otuskotlin.aiassistant.repo.tests.*
import ru.otus.otuskotlin.aiassistant.repo.common.ModelRepoInitialized
import ru.otus.otuskotlin.aiassistant.repo.inmemory.ModelRepoInMemory

class ModelRepoInMemoryCreateTest : RepoModelCreateTest() {
    override val repo = ModelRepoInitialized(
        ModelRepoInMemory(randomUuid = { uuidNew.asString() }),
        initObjects = initObjects,
    )
}

class ModelRepoInMemoryDeleteTest : RepoModelDeleteTest() {
    override val repo = ModelRepoInitialized(
        ModelRepoInMemory(),
        initObjects = initObjects,
    )
}

class ModelRepoInMemoryReadTest : RepoModelReadTest() {
    override val repo = ModelRepoInitialized(
        ModelRepoInMemory(),
        initObjects = initObjects,
    )
}

class ModelRepoInMemorySearchTest : RepoModelSearchTest() {
    override val repo = ModelRepoInitialized(
        ModelRepoInMemory(),
        initObjects = initObjects,
    )
}

class ModelRepoInMemoryUpdateTest : RepoModelUpdateTest() {
    override val repo = ModelRepoInitialized(
        ModelRepoInMemory(randomUuid = { lockNew.asString() }),
        initObjects = initObjects,
    )
}
