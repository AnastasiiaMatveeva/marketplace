package ru.otus.otuskotlin.aiassistant.repo.postgresql

actual fun getEnv(name: String): String? = System.getenv(name)
