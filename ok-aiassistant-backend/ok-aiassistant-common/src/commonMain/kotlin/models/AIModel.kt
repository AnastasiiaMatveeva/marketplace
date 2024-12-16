package models

data class AIModel(
    var id: AIModelId = AIModelId.NONE,
    var title: String = "",
    var description: String = "",
    var modelParams: List<AIModelParam> = emptyList(),
    var solverPath: String = "",
    var scriptPath: String = "",
    var features: Array<Double> = emptyArray(),
    var results: Array<Double> = emptyArray(),
    var ownerId: AIUserId = AIUserId.NONE,
    var visibility: AIVisibility = AIVisibility.NONE,
    val permissionsClient: MutableSet<AIModelPermissionClient> = mutableSetOf(),
    var lock: AIModelLock = AIModelLock.NONE,

//    var materials: Map<Material, Double> = mapOf(),
) {
//    fun getMaterialsUsage(): Map<Material, Double> {
//        val result = mutableMapOf<Material, Double>()
//        materials.forEach { (material, quantity) ->
//            val currentQuantity = result[material] ?: 0.0
//            result[material] = currentQuantity + quantity
//        }
//        return result
//    }

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = AIModel()
    }

}
