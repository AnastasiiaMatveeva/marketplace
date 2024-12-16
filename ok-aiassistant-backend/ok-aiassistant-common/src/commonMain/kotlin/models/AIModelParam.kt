package models

data class AIModelParam(
    var id: AIParamId = AIParamId.NONE,
    var paramType: AIParamType = AIParamType.NONE,
    var bounds: ParamBounds = ParamBounds(),
    var line: Int = 0,
    var position: Int = 0,
    var separator: String = "",
    var name: String = "",
) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = AIModelParam()
    }

}