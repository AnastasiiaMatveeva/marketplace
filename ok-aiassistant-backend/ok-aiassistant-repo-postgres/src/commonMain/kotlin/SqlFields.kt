package ru.otus.otuskotlin.aiassistant.repo.postgresql

object SqlFields {
    const val ID = "id"
    const val TITLE = "title"
    const val DESCRIPTION = "description"

    const val VISIBILITY = "visibility"
    const val LOCK = "lock"
    const val OWNER_ID = "owner_id"

    const val SCRIPT_PATH = "script_path"
    const val SOLVER_PATH = "solver_path"

    const val PARAMS = "params"
    const val STATUS = "status"

    const val FEATURES = "features"
    const val RESULTS = "results"

    const val VISIBILITY_TYPE = "model_visibilities_type"
    const val VISIBILITY_PUBLIC = "public"
    const val VISIBILITY_OWNER = "owner"
    const val VISIBILITY_GROUP = "group"
}
