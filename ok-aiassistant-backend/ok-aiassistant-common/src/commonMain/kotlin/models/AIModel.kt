package models

data class AIModel(
    var id: AIModelId = AIModelId.NONE,
    var title: String = "",
    var description: String = "",
    var modelParams: MutableList<AIModelParam> = mutableListOf(),
    var solverPath: String = "",
    var scriptPath: String = "",
    var features: Array<Double> = emptyArray(),
    var results: Array<Double> = emptyArray(),
    var ownerId: AIUserId = AIUserId.NONE,
    var visibility: AIVisibility = AIVisibility.NONE,
    val permissionsClient: MutableSet<AIModelPermissionClient> = mutableSetOf(),
    var lock: AIModelLock = AIModelLock.NONE,

) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = AIModel()
    }

    fun deepCopy(): AIModel = copy()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AIModel) return false

        return id == other.id &&
                title == other.title &&
                description == other.description &&
                modelParams == other.modelParams &&
                solverPath == other.solverPath &&
                scriptPath == other.scriptPath &&
                features.contentEquals(other.features) &&
                results.contentEquals(other.results) &&
                ownerId == other.ownerId &&
                visibility == other.visibility &&
                permissionsClient == other.permissionsClient &&
                lock == other.lock
    }

}
