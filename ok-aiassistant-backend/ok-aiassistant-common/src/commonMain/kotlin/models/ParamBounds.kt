package models

data class ParamBounds (
    var min: Double =  Double.NaN,
    var max: Double =  Double.NaN,


) {
    fun isEmpty(): Boolean {
        return min.isNaN() || max.isNaN()
    }

    companion object {
        private val NONE = ParamBounds()
    }
}