[![Build Status](https://travis-ci.com/Hexlet/hexlet-correction.svg?branch=master)](https://travis-ci.com/Hexlet/hexlet-correction)
[![Maintainability](https://api.codeclimate.com/v1/badges/adb867526033eca72d49/maintainability)](https://codeclimate.com/github/Hexlet/hexlet-correction/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/adb867526033eca72d49/test_coverage)](https://codeclimate.com/github/Hexlet/hexlet-correction/test_coverage)

# hexlet-correction

Сервис для отправки с сайтов ошибок пользователями (к примеру, выделяем текст с ошибкой и отправляем через вызов формы Ctrl + Enter), для дальнейшей обработки клиентом сервиса.

### Участие

* Обсуждение в канале #java слака http://slack-ru.hexlet.io

### Требования к системе для запуска и разработки (без учета OS)

* Java 12

### Стек технологий проекта

* Java 12
* Spring Boot 2
* PostgreSQL (liquibase для миграций)
* Frontend - ReactJs

### Компиляция, запуск и работа с проектом

```bash
$ make # build & run
$ make test # compile and tests
```
Все миграции схемы базы хранятся в `/src/main/resources/db/changelog/` используется liquibase для генерации и обновления схемы.
При локальной разработке используется in memory база H2 в проде PostgreSQL.

Для генерации новой миграции, запустите:

```bash
$ make generate-migration
```
И на основании изменений Entity сгенирируется новый changeset.

### MVP

* Регистрация пользователей сервиса
* Генерация JS кода для установки на сайт
* Отправка на сервис ошибок (через REST API)
* Возможность просмотра ошибок пользователем в интерфейсе сервиса

##
[![Hexlet Ltd. logo](https://raw.githubusercontent.com/Hexlet/hexletguides.github.io/master/images/hexlet_logo128.png)](https://ru.hexlet.io/pages/about?utm_source=github&utm_medium=link&utm_campaign=exercises-java)

This repository is created and maintained by the team and the community of Hexlet, an educational project. [Read more about Hexlet (in Russian)](https://ru.hexlet.io/pages/about?utm_source=github&utm_medium=link&utm_campaign=exercises-java).
##