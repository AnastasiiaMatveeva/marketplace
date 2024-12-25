package ru.otus.otuskotlin.aiassistant.biz.validation

import models.AICommand
import kotlin.test.Test

class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: AICommand = AICommand.CREATE

    @Test fun correctTitle() = validationTitleCorrect(command, processor)
    @Test fun trimTitle() = validationTitleTrim(command, processor)
    @Test fun emptyTitle() = validationTitleEmpty(command, processor)
    @Test fun badSymbolsTitle() = validationTitleSymbols(command, processor)

    @Test fun correctDescription() = validationDescriptionCorrect(command, processor)
    @Test fun trimDescription() = validationDescriptionTrim(command, processor)
    @Test fun emptyDescription() = validationDescriptionEmpty(command, processor)
    @Test fun badSymbolsDescription() = validationDescriptionSymbols(command, processor)

    @Test fun correctParams() = validationParamsCorrect(command, processor)
    @Test fun badParamPosition() = validationParamsBadPosition(command, processor)
    @Test fun badParamType() = validationParamsBadType(command, processor)
    @Test fun badParamBoundsSize() = validationParamsBadBoundsMinMax(command, processor)
    @Test fun badParamBoundsValues() = validationParamsBadBoundsExist(command, processor)


}
