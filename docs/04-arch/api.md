### API

#### Описание используемых сущностей:

1. **SurrogateModel (Суррогатная модель)**

* Основные параметры (поля):
  1. id
  2. title: название модели
  3. description: описание модели
  4. parameters: входные параметры модели
  5. solverPath: путь к внешнему решателю
  6. scriptPath: путь к входному макросу
  7. visibility: тип видимости модели (ownerOnly, registeredOnly, public)
  8. userId: идентификатор пользователь

2. **ModelParam (Параметры модели)**

* Основные параметры (поля)  
  1. name: имя параметра
  2. param_type: тип параметра (например, continuous, discrete)
  3. line: номер строки в макросе
  4. position: номер позиции параметра в строке
  5. separator: разделитель символов в строке
  6. bounds: границы варьирования параметра

3. **Bounds (Границы варьирования параметра)**

* Основные параметры (поля)
  1. min: минимальное допустимое значение параметра
  2. max: максимальное допустимое значение параметра

#### Функции (эндпоинты):

  1. CRUDS (create, read, update, delete, search)
  2. train: обучение модели с использованием данных
  3. predict: предсказания на основе обученной модели
