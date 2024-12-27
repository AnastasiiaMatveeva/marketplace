package ru.otus.otuskotlin.aiassistant.biz.exceptions

import models.AIWorkMode

class AIModelDbNotConfiguredException(val workMode: AIWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
