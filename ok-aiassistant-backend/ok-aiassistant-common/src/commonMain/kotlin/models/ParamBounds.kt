package models

data class ParamBounds (
    var min: Double =  Double.NaN,
    var max: Double =  Double.NaN,


) {
    fun isEmpty() = this == NONE

    companion object {
        private val NONE = ParamBounds()
    }
}