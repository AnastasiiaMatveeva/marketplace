package models

data class ModelFilter(
    var searchString: String = "",
    var ownerId: UserId = UserId.NONE,
)
