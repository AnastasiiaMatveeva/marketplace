package ru.otus.otuskotlin.aiassistant.biz.validation

import models.AICommand
import kotlin.test.Test

class BizValidationReadTest: BaseBizValidationTest() {
    override val command = AICommand.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}
