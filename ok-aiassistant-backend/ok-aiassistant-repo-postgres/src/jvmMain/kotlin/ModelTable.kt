package ru.otus.otuskotlin.aiassistant.repo.postgresql

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeToSequence
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import models.*
import org.jetbrains.exposed.sql.json.jsonb

class ModelTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val title = text(SqlFields.TITLE).nullable()
    val description = text(SqlFields.DESCRIPTION).nullable()
    val solver_path = text(SqlFields.SOLVER_PATH).nullable()
    val script_path = text(SqlFields.SCRIPT_PATH).nullable()
    val owner = text(SqlFields.OWNER_ID)
    val visibility = visibilityEnumeration(SqlFields.VISIBILITY)
    val lock = text(SqlFields.LOCK)
    val features = array<String>(SqlFields.FEATURES).nullable()
    val results = array<String>(SqlFields.RESULTS).nullable()
    val status = text(SqlFields.STATUS).nullable()
    val params = jsonb(
        SqlFields.PARAMS,
        { value ->
            Json.encodeToString(
                JsonParams.serializer(),
                value
            )
        },
        { value ->
            Json.decodeFromString(
                JsonParams.serializer(),
                value
            )
        }
    ).nullable()

    override val primaryKey = PrimaryKey(id)



    fun from(res: ResultRow): AIModel {
        return AIModel(
            status = res[status] ?: "",
            id = AIModelId(res[id].toString()),
            title = res[title] ?: "",
            description = res[description] ?: "",
            ownerId = AIUserId(res[owner].toString()),
            solverPath = res[solver_path] ?: "",
            scriptPath = res[script_path] ?: "",
            visibility = res[visibility],
            features = res[features]?.map { it.toDouble() }?.toTypedArray() ?: arrayOf(),
            results = res[results]?.map { it.toDouble() }?.toTypedArray() ?: arrayOf(),
            modelParams = res[params]?.params?.map { it.toAIModelParam() }?.toMutableList() ?: mutableListOf(),
            lock = AIModelLock(res[lock]),
            )
    }

    private fun JsonParam.toAIModelParam() = AIModelParam(
        paramType = AIParamType.valueOf(type),
        bounds = bounds.toParamBounds(),
        line = line,
        position = position,
        separator = separator,
        name = name,
    )

    private fun AIModelParam.toJsonParam() = JsonParam(
        type = paramType.name,
        bounds = bounds.toJsonBounds(),
        line = line,
        position = position,
        separator = separator,
        name = name,
    )

    private fun JsonBounds.toParamBounds() = ParamBounds(
        min = min,
        max = max,
    )

    private fun ParamBounds.toJsonBounds() = JsonBounds(
        min = min,
        max = max,
    )

    fun to(it: UpdateBuilder<*>, model: AIModel, randomUuid: () -> String) {
        it[status] = model.status
        it[id] = model.id.takeIf { it != AIModelId.NONE }?.asString() ?: randomUuid()
        it[title] = model.title
        it[description] = model.description
        it[owner] = model.ownerId.asString()
        it[solver_path] = model.solverPath
        it[script_path] = model.scriptPath
        it[visibility] = model.visibility
        it[features] = model.features.map { it.toString() }
        it[results] = model.results.map { it.toString() }
        it[params] = JsonParams(model.modelParams.map { it.toJsonParam() })
        it[lock] = model.lock.takeIf { it != AIModelLock.NONE }?.asString() ?: randomUuid()
    }

}
