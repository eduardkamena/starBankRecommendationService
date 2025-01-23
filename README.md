# Recommendation Service

Микросервис для рекомендации банковских продуктов клиентам банка «Стар».

Данный сервис разрабатывается в рамках курсовой работы.
Сервис проверяет возможность рекомендовать клиенту один из банковских продуктов. Рекомендации подбираются персонально для каждого клиента, после проверки определенных условий одного из продуктов.

## Условие задачи

Мы — банк «Стар». Наша цель — внедрить рекомендательную систему для улучшения маркетинга и помощи сотрудникам в предложении персонализированных услуг клиентам.

Задача — создать сервис, который будет рекомендовать клиентам новые банковские продукты на основе динамических правил рекомендаций.

## Входные данные сервиса

Сервис использует начальные данные из уже наполненной базы данных. В реальных условиях эта база данных находится в инфраструктуре компании, но для микросервиса используется встраиваемая файловая база данных H2.

### Основные сущности базы данных

1. **Пользователи банка**.
2. **Продукты банка**.
3. **Операции пользователей по продуктам банка**.

Эти сущности не управляются приложением напрямую (не используются JPA и Entity). Вместо этого используется `JDBC` и `JdbcTemplate`.

### Базы данных

Наполненная база данных содержит все необходимые таблицы и уже заполнена данными для выдачи рекомендаций. Файл базы данных: `transaction.mv.db`.

Для просмотра базы данных можно использовать DBeaver:
1. Создайте соединение с базой данных H2.
2. Укажите путь к файлу базы данных.

**Важно**: База данных доступна только для чтения.

Помимо исходной базы данных к проекту для создания сущностей и дополнительной работы с динамическими рекомендациями была подключена вторая база данных, которая использует JPA и Entity

## Продукты для динамических рекомендаций

Продукты для рекомендаций включают:
- Название и тип продукта.
- Текстовое описание продукта.
- Набор правил, при выполнении которых продукт попадает в список рекомендаций для пользователя.

### Динамические правила

Динамическое правило представляет собой массив объектов запросов. Каждый объект запроса описывается следующими полями:
- `query` — тип запроса.
- `arguments` — аргументы запроса.
- `negate` — модификатор отрицания.

#### Пример динамического правила в JSON

```json
{
    "query": "USER_OF",
    "arguments": [
        "CREDIT"
    ],
    "negate": true
}
```

Этот запрос проверяет, является ли пользователь клиентом продукта типа CREDIT. Если negate равно true, то правило выполняется, если пользователь не является клиентом продукта.

#### Типы запросов
`USER_OF` — проверяет, является ли пользователь клиентом продукта.

`Аргументы:` тип продукта **(DEBIT, CREDIT, INVEST, SAVING)**.

#### Пример динамической рекомендации в JSON

```json
{
    "productName": "Invest 500",
    "productId": "147f6a0f-3b91-413b-ab99-87f081d60d5a",
    "productText": "Описание Invest 500",
    "rule": [
        {
            "query": "USER_OF",
            "arguments": [
                "DEBIT"
            ],
            "negate": true
        },
        {
            "query": "USER_OF",
            "arguments": [
                "INVEST"
            ],
            "negate": false
        },
        {
            "query": "TRANSACTION_SUM_COMPARE",
            "arguments": [
                "SAVING",
                "DEPOSIT",
                ">",
                "1000"
            ],
            "negate": true
        }
    ]
}
```

## Основные технологии

- **Java 17** — основной язык разработки.
- **Spring Boot** — фреймворк для создания микросервисов.
- **Spring Data JPA** — для работы с базой данных.
- **Spring JDBC** — для выполнения SQL-запросов.
- **H2 Database** — встраиваемая база данных для тестирования и разработки.
- **PostgreSQL** — основная база данных для хранения данных.
- **Liquibase** — для управления миграциями базы данных.
- **Caffeine** — кэширование данных.
- **Telegram Bot API** — для интеграции с Telegram.
- **Swagger (OpenAPI 3.0)** — для документирования API.

## Дополнительные библиотеки

- **Lombok** — для сокращения boilerplate-кода.
- **Apache Commons Collections** — для работы с коллекциями.
- **Apache Commons Lang** — для работы с утилитами.
- **Emoji-Java** — для работы с эмодзи в Telegram.
- **Spring Boot Actuator** — для мониторинга и управления приложением.

## Функционал бэкенда

1. Рекомендации продуктов:
   - Фиксированные рекомендации на основе предопределенных правил.
   - Динамические рекомендации на основе пользовательских запросов.
   - Кэширование результатов для повышения производительности.
2. Управление рекомендациями:
   - Создание, получение, обновление и удаление динамических рекомендаций.
   - Получение статистики срабатывания рекомендаций.
3. Интеграция с Telegram:
   - Отправка рекомендаций пользователям через Telegram-бота.
   - Обработка команд пользователя (например, /recommend).
4. Документирование API:
   - Автоматическая генерация документации с использованием Swagger.
5. Управление кэшем:
   - Сброс кэша рекомендаций через API.
6. Мониторинг:
   - Интеграция с Spring Boot Actuator для мониторинга состояния приложения.

## Рекомендации через Telegram-бота

Дополнительо создан Telegram-бот, который выдает рекомендации пользователям по их username.

### Протокол работы бота

1. Приветствие и справка:
   - При первом обращении бот приветствует пользователя и выводит справку с доступными командами.
2. Команда `/recommend username`:
   - Бот принимает команду `/recommend` с указанием имени пользователя (username).
   - Если пользователь найден, бот возвращает рекомендации в формате:
    ```
    Здравствуйте, <Имя и фамилия пользователя>!

    Новые продукты для вас:
   - <Название продукта>: <Описание продукта>
   - <Название продукта>: <Описание продукта>
    ```
   - Если пользователь не найден или найдено несколько пользователей, бот выдает сообщение: **«Пользователь не найден»**.

### Пример работы бота

1. Пользователь отправляет команду:
```
/recommend luke.skywalker
```
2. Бот отвечает:
```
Здравствуйте, Luke Skywalker!

Новые продукты для вас:
- Кредитная карта: Удобная кредитная карта с кэшбэком до 10%.
- Инвестиционный счет: Начните инвестировать с минимальной суммой.
```
3. Если пользователь не найден:
```
Пользователь не найден.
```

## Статистика срабатывания правил рекомендаций

Для анализа эффективности рекомендаций ведется статистика срабатывания динамических правил. Каждый раз, когда правило срабатывает и рекомендация выдается пользователю, счетчик увеличивается.

### Возможности:
- Получение статистики по всем рекомендациям.
- Анализ популярности и эффективности рекомендаций.

### Пример статистики:

```json
[
  {
    "recommendations_id": "59efc529-2fff-41af-baff-90ccd7402925",
    "count": 42
  },
  {
    "recommendations_id": "147f6a0f-3b91-413b-ab99-87f081d60d5a",
    "count": 15
  }
]
```

## Запуск проекта
1. Убедитесь, что у вас установлены:
   - Java 17
   - Maven
   - База данных H2 (если требуется локальное подключение).
   - PostgreSQL
2. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/eduardkamena/starBankRecommendationService.git
    ```
3. Настройте базу данных:
    - Создайте базу данных в PostgreSQL.
    - Обновите настройки в `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/ваша-база-данных
   spring.datasource.username=ваш-username
   spring.datasource.password=ваш-пароль
   ```
4. Настройте Telegram бота:
    - Создайте бота через [BotFather](https://core.telegram.org/bots#botfather).
    - Укажите токен бота в `application.properties`:
   ```properties
   telegram.bot.token=ваш-токен
   ```
5. Соберите проект с помощью Maven:
    ```bash
    mvn clean install
    ```
6. Запустите приложение:
    ```bash
    mvn spring-boot:run
    ```
7. Для запуска тестов используйте:
    ```bash
    mvn test
    ```
8. Сервис будет доступен по адресу: [http://localhost:8085](http://localhost:8085).

## API

Документация API доступна по адресу: `http://localhost:8085/swagger-ui.html`.

**Основные endpoints**
- POST /rule — создание динамической рекомендации.
- GET /rule/{ruleId} — получение динамической рекомендации по ID.
- DELETE /rule/{ruleId} — удаление динамической рекомендации по ID.
- GET /rule/allRules — получение всех динамических рекомендаций.
- GET /rule/stats — получение статистики срабатывания рекомендаций.
- GET /product/{productId} — получение продукта рекомендации по ID.
- GET /recommendation/fixed/{userId} — получение фиксированных рекомендаций для пользователя.
- GET /recommendation/dynamic/{userId} — получение динамических рекомендаций для пользователя.
- POST /management/clear-caches — сброс кэша рекомендаций.
- GET /management/info — получение информации о сервисе.).

## Тестирование

Для тестирования использовались:

- **H2 in-memory** — для работы с базой данных.
- **WebMvcTests** — для тестирования контроллеров.
- **Unit Tests** — для тестирования сервисов и репозиториев.

## Работа с готовыми динамическими рекомендациями

- Готовые динамические рекомендации находятся в папке `resources/dynamicRecommendations/JSON`.
- Внести динамическую рекомендацию в базу данных можно с помощью запроса `POST /rule`.
- Пользователи, соответствующие динамическим рекомендациям находятся в папке `resources/dynamicRecommendations/userTest`. 
- Проверку пользователя можно сделать с помощью запроса `GET /recommendation/dynamic/{userId}`

## Проблемы и отзывы

Если вы столкнулись с какой-то проблемой или у вас есть предложения, пожалуйста, откройте тему на [GitHub Issues](https://github.com/eduardkamena/starBankRecommendationService/issues) 

## Над проектом работали
 - Эдуард Каменских — [eduardkamena](https://github.com/eduardkamena)
 - Олег Симаков — [gitWestender](https://github.com/gitWestender)
 - Александр Говорин — [Zhizhna](https://github.com/Zhizhna)


