package ru.otus.otuskotlin.aiassistant.biz.validation

import models.*
import kotlin.test.Test

class BizValidationPredictTest: BaseBizValidationTest() {
    override val command = AICommand.PREDICT

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctFeatures() = validationFeaturesCorrect(command, processor)
    @Test fun emptyFeatures() = validationFeaturesEmpty(command, processor)
    @Test fun badSizeFeatures() = validationFeaturesSize(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}
