### Функциональные требования

1. **Регистрация и управление учетной записью**  
   1.1. **Регистрация в программе**
    - *Описание требования:* Реализовать функционал регистрации на платформе с обязательными полями для заполнения и подтверждением через электронную почту. Поля для заполнения включают:
        - ФИО
        - Должность
        - Электронная почта (обязательное поле)
        - Пароль (обязательное поле)

   1.2. **Вход в систему**
    - *Описание требования:* Обеспечить возможность аутентификации пользователей по электронной почте и паролю.

2. **Управление проектами и моделями**  
   2.1. **Создание и управление проектами**
    - *Описание требования:* Реализовать возможность создания, редактирования и удаления проектов.

   2.2. **Создание и управление моделями**
    - *Описание требования:* Реализовать возможность создания, редактирования и удаления суррогатных моделей в рамках каждого проекта.

   2.3. **Версионность моделей**
    - *Описание требования:* Реализовать поддержку версионности моделей.

3. **Управление цифровыми двойниками**  
   3.1. **Создание цифрового двойника**
    - *Описание требования:* Реализовать возможность создания цифрового двойника для инженерного объекта на основе одной или нескольких суррогатных моделей.

   3.2. **Отслеживание состояния цифрового двойника в реальном времени**
    - *Описание требования:* Обеспечить визуализацию данных цифрового двойника в реальном времени, включая параметры объекта, прогнозы и отклонения.

   3.3. **Обработка и управление данными**
    - *Описание требования:* Реализовать возможность загрузки данных для тренировки, валидации и тестирования моделей.

4. **Анализ входных данных**  
    - *Описание требования:* Автоматически анализировать данные на наличие ошибок и выбросов перед использованием в модели.

5. **Мониторинг и управление процессами**  
   5.1. **Учет фактического расхода**
    - *Описание требования:* Обеспечить возможность отслеживания работы модели, включая состояние процесса и прогнозируемые данные.

   5.2. **Оповещения о критических состояниях**
    - *Описание требования:* Реализовать систему оповещений в случае отклонений от нормального состояния объекта, прогнозируемого моделью.

6. **Расчет метрик и анализ результатов**  
   6.1. **Сравнение фактических и прогнозируемых данных**
    - *Описание требования:* Реализовать возможность сравнения прогнозов модели с фактическими данными, предоставляя визуализацию отклонений.

7. **Интеграция с внешними системами**  
   7.1. **Подключение к датчикам и источникам данных**
    - *Описание требования:* Обеспечить интеграцию с датчиками и системами, предоставляющими данные для моделей и цифровых двойников.  
