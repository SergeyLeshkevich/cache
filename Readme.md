# Web-servlet приложение

## В данном проекте было реализованно: 
* реализация кэша с использованием алгоритмов LRU и LFU.
* В приложении созданны слои service и dao.
* В этих сервисах реализованны CRUD операции для работы с entity.Работа арганизованна через интерфейсы. 
* Результат работы dao синхронизован с кешем через proxy.
* Алгоритм и максимальный размер кэша считывается с файла resources/application.yml.
* Service работает с dto. Объект dto валидируется путем дабовления над полем аннотации и с regex методом.
* Кэши и прослойка service покрыты unit tests.
* Проект содержит javadoc в директории resources. 
* В файле application.log содержаться логи запуска проекта с демонстрациией работы proxy и кэша LFU.
* Реализованна возможность печати документов в файл .pdf. Образец полученного файла находится в пакете /resources.
## Добавлена web часть проекта.
- Добавлены сервлеты:
* для CRUD операций;
* сервлет для получения лимитированного списка объектов (лимит задается в файле resources/application.yml)
по номеру web страницы. Если лимит не задан, то по умолчанию берется 20.
* сервлет для печати списка объектов в .pdf файл. Файл по умолчанию сохраняется на сервере.
- Добавлен EncodingFilter.
  
## Для запуска проекта на компьютере должна стоять база данных Postgres и содержать базу данных
cash_db. При первом запуске приложения библиотека liquibase создаст 
необходимые таблицы и заполнит их значениями, а также файл application.log будет дополнен логами от нового запуска. 
## В пакете /resources содержится файл web-app.postman_collection.json для работы с postman.
## Url:
- http://{{host}}/web_app/cars/file?uuid=1be4206e-787f-4708-924f-1adf9dd9a5da (GET)
- http://{{host}}/web_app/cars/page?numberPage=3 (GET)
- http://{{host}}/web_app/cars?brand=Peugeot&model=500800&b_type=minivan&en_capacity=1.6&fuel_type=diesel (POST)
- http://{{host}}/web_app/cars?uuid=e4866644-fd55-4cfc-8e56-baff0fef3295 (DELETE) 
- http://{{host}}/web_app/cars?uuid=1be4206e-787f-4708-924f-1adf9dd9a5da&brand=Peugeot&model=5008&b_type=minivan&en_capacity=1.5&fuel_type=diesel (PUT)
- http://{{host}}/web_app/car?uuid=1be4206e-787f-4708-924f-1adf9dd9a5da (GET)