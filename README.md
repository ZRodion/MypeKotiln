# MypeKotlin

При входе в приложение, гугл-карта отображает местоположение устройства на карте, а так же расставляет маркеры в точках, где находятся станции метро города Минска. Информацию о том, где их искать, приложение получает посредством обращения к арi  https://my-json-server.typicode.com/, используя библиотеку **retrofit**. За парсинг ответа отвечает **MoshiConverter**. Пользователь также наделен фрагментом, где он может сохранять данные о себе(фото камеры и текст). Данные о пользователе сохраняются в базу данных с помощью библиотеки **Room**. Во фрагменте, отвечающем за настройки, есть функции для смены языка(русс\англ), открытие приложения в google play и отправка сообщения(с предварительно заполненными полями кому отправлять и тема письма). Для навигации между фрагментами используется **Navigation component**

### TO-DO LIST:
- [X] Create Readme
- [X] Add current location
- [ ] Dagger 2
- [ ] Testing
- [X] Login
