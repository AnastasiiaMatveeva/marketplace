package models

data class AIModelFilter(
    var searchString: String = "",
    var ownerId: AIUserId = AIUserId.NONE,
)
