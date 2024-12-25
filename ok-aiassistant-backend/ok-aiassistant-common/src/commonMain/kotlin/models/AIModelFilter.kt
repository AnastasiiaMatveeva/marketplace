package models

data class AIModelFilter(
    var searchString: String = "",
    var ownerId: AIUserId = AIUserId.NONE,
){
    fun deepCopy(): AIModelFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = AIModelFilter()
    }
}