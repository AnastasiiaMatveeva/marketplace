package ru.otus.otuskotlin.aiassistant.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import models.AIVisibility

fun Table.visibilityEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.VISIBILITY_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.VISIBILITY_OWNER -> AIVisibility.VISIBLE_TO_OWNER
            SqlFields.VISIBILITY_GROUP -> AIVisibility.VISIBLE_TO_GROUP
            SqlFields.VISIBILITY_PUBLIC -> AIVisibility.VISIBLE_PUBLIC
            else -> AIVisibility.NONE
        }
    },
    toDb = { value ->
        when (value) {
//            MkplVisibility.VISIBLE_TO_OWNER -> PGobject().apply { type = SqlFields.VISIBILITY_TYPE; value = SqlFields.VISIBILITY_OWNER}
            AIVisibility.VISIBLE_TO_OWNER -> PgVisibilityOwner
            AIVisibility.VISIBLE_TO_GROUP -> PgVisibilityGroup
            AIVisibility.VISIBLE_PUBLIC -> PgVisibilityPublic
            AIVisibility.NONE -> throw Exception("Wrong value of Visibility. NONE is unsupported")
        }
    }
)

sealed class PgVisibilityValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.VISIBILITY_TYPE
        value = eValue
    }
}

object PgVisibilityPublic: PgVisibilityValue(SqlFields.VISIBILITY_PUBLIC) {
    private fun readResolve(): Any = PgVisibilityPublic
}

object PgVisibilityOwner: PgVisibilityValue(SqlFields.VISIBILITY_OWNER) {
    private fun readResolve(): Any = PgVisibilityOwner
}

object PgVisibilityGroup: PgVisibilityValue(SqlFields.VISIBILITY_GROUP) {
    private fun readResolve(): Any = PgVisibilityGroup
}
