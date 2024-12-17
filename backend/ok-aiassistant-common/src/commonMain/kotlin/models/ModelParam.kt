package models

data class ModelParam(
    var id: ParamId = ParamId.NONE,
    var paramType: ParamType = ParamType.NONE,
    var line: Int? = null,
    var position: Int? = null,
    var separator: String = "",
    var name: String? = "",
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = ModelParam()
    }

}