package ru.otus.otuskotlin.aiassistant.repo.postgresql

object SqlFields {
    const val ID = "id"
    const val TITLE = "title"
    const val DESCRIPTION = "description"

    const val VISIBILITY = "visibility"
    const val LOCK = "lock"
    const val LOCK_OLD = "lock_old"
    const val OWNER_ID = "owner_id"

    const val SCRIPT_PATH = "script_path"
    const val SOLVER_PATH = "solver_path"


    const val FEATURES = "features"
    const val RESULTS = "results"

    const val VISIBILITY_TYPE = "model_visibilities_type"
    const val VISIBILITY_PUBLIC = "public"
    const val VISIBILITY_OWNER = "owner"
    const val VISIBILITY_GROUP = "group"

    const val FILTER_TITLE = TITLE
    const val FILTER_OWNER_ID = OWNER_ID

    const val DELETE_OK = "DELETE_OK"

    fun String.quoted() = "\"$this\""
    val allFields = listOf(
        ID, TITLE, DESCRIPTION, VISIBILITY, LOCK,
        LOCK_OLD, OWNER_ID, SCRIPT_PATH, SOLVER_PATH
    )
}
