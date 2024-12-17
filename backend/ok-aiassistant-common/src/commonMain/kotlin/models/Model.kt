package models

data class Model(
    var id: ModelId = ModelId.NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: UserId = UserId.NONE,
    var visibility: Visibility = Visibility.NONE,
    val permissionsClient: MutableSet<ModelPermissionClient> = mutableSetOf(),
    var lock: ModelLock = ModelLock.NONE,
    var modelParams: List<ModelParam> = emptyList()

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
        private val NONE = Model()
    }

}
