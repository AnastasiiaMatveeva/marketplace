package ru.otus.otuskotlin.aiassistant.biz.validation

import models.AICommand
import kotlin.test.Test


class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = AICommand.SEARCH

    @Test fun correctSearchString() = validationSearchStringCorrect(processor)
    @Test fun trimSearchString() = validationSearchStringTrim(processor)
    @Test fun tooSmallSearchString() = validationSearchStringTooShort(processor)
    @Test fun tooLongSearchString() = validationSearchStringTooLong(processor)
}
