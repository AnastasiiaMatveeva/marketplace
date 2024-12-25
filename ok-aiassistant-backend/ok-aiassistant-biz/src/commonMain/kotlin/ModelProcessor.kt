package ru.otus.otuskotlin.aiassistant.biz

import ru.otus.otuskotlin.aiassistant.biz.general.initStatus
import ru.otus.otuskotlin.aiassistant.biz.general.operation
import ru.otus.otuskotlin.aiassistant.biz.general.stubs
import ru.otus.otuskotlin.aiassistant.biz.stubs.*
import ru.otus.otuskotlin.aiassistant.biz.validation.*
import ru.otus.otuskotlin.aiassistant.common.CorSettings
import ru.otus.otuskotlin.aiassistant.cor.rootChain
import ru.otus.otuskotlin.aiassistant.cor.worker

import models.AICommand
import models.AIModelLock
import models.AIModelId
import AppContext

class ModelProcessor(
    private val corSettings: CorSettings = CorSettings.NONE
) {
    suspend fun exec(ctx: AppContext) = businessChain.exec(ctx.also { it.corSettings = corSettings })
    private val businessChain = rootChain {
        initStatus("Инициализация статуса")
        operation("Создание модели", AICommand.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadTitle("Имитация ошибки валидации имени модели")
                stubValidationBadDescription("Имитация ошибки валидации описания модели")
                stubValidationBadParamType("Имитация ошибки валидации типа параметра")
                stubValidationBadParamPosition("Имитация ошибки валидации позиции параметра в строке")
                stubValidationBadParamBounds("Имитация ошибки валидации границ диапозона варьирования параметра")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }
                worker("Очистка id") { modelValidating.id = AIModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля name") { modelValidating.title = modelValidating.title.trim() }
                worker("Очистка поля description") { modelValidating.description = modelValidating.description.trim() }

                validateTitleNotEmpty("Проверка наличия поля title")
                validateTitleHasContent("Проверка поля title")
                validateDescriptionNotEmpty("Проверка наличия поля title")
                validateDescriptionHasContent("Проверка поля title")

                validateParamType("Проверка типа параметра")
                validateParamBoundsExist("Проверка размера списка paramValues")
                validateParamBoundsMinMax("Проверка соответствия границ мин и макс значениям paramValues")
                validateParamPosition("Проверка позиции параметра в файле")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Получить модель", AICommand.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }
                worker("Очистка id") { modelValidating.id = AIModelId(modelValidating.id.asString().trim()) }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Изменить модель", AICommand.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadTitle("Имитация ошибки валидации названия модели")
                stubValidationBadDescription("Имитация ошибки валидации описания модели")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubValidationBadParamType("Имитация ошибки валидации типа параметра")
                stubValidationBadParamPosition("Имитация ошибки валидации позиции параметра в строке")
                stubValidationBadParamBounds("Имитация ошибки валидации границ диапозона варьирования параметра")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }
                worker("Очистка id") { modelValidating.id = AIModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля lock") { modelValidating.lock = AIModelLock(modelValidating.lock.asString().trim()) }
                worker("Очистка поля title") { modelValidating.title = modelValidating.title.trim() }
                worker("Очистка поля description") { modelValidating.description = modelValidating.description.trim() }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                validateTitleNotEmpty("Проверка наличия поля title")
                validateTitleHasContent(      "Проверка поля title")

                validateDescriptionNotEmpty("Проверка наличия поля description")
                validateDescriptionHasContent(      "Проверка поля description")

                validateParamType("Проверка типа параметра")
                validateParamBoundsExist("Проверка размера списка paramValues")
                validateParamBoundsMinMax("Проверка соответствия границ мин и макс значениям paramValues")
                validateParamPosition("Проверка позиции параметра в файле")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Удалить модель", AICommand.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }

                worker("Очистка id") { modelValidating.id = AIModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля lock") { modelValidating.lock = AIModelLock(modelValidating.lock.asString().trim()) }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Поиск моделей", AICommand.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadSearchString("Имитация ошибки валидации поисковой строки")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelFilterValidating") {
                    modelFilterValidating = modelFilterRequest.deepCopy()
                }

                validateSearchStringLength("Валидация длины строки поиска в фильтре")
                finishModelFilterValidation("Завершение проверок")
            }
        }

        operation("Обучить модель", AICommand.TRAIN) {
            stubs("Обработка стабов") {
                stubTrainSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }

                worker("Очистка id") { modelValidating.id = AIModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля lock") { modelValidating.lock = AIModelLock(modelValidating.lock.asString().trim()) }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Предсказать при помощи модели", AICommand.PREDICT) {
            stubs("Обработка стабов") {
                stubPredictSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubValidationBadFeatures("Имитация ошибки валидации входного массива параметров")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }

                worker("Очистка id") { modelValidating.id = AIModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля lock") { modelValidating.lock = AIModelLock(modelValidating.lock.asString().trim()) }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                validateFeaturesNotEmpty("Проверка наличия массива из входных параметров")
                validateFeatures("Проверка наличия соответствии размера входного массива с количеством параметров")

                finishModelValidation("Завершение проверок")
            }
        }
    }.build()
}