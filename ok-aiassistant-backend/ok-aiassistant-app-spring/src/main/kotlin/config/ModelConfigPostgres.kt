package ru.otus.otuskotlin.aiassistant.app.spring.config

import org.springframework.boot.context.properties.ConfigurationProperties
import ru.otus.otuskotlin.aiassistant.repo.postgresql.SqlProperties

@ConfigurationProperties(prefix = "psql")
data class ModelConfigPostgres(
    var host: String = "localhost",
    var port: Int = 5432,
    var user: String = "postgres",
    var password: String = "marketplace-pass",
    var database: String = "marketplace-models",
    var schema: String = "public",
    var table: String = "models",
) {
    val psql: SqlProperties = SqlProperties(
        host = host,
        port = port,
        user = user,
        password = password,
        database = database,
        schema = schema,
        table = table,
    )
}
