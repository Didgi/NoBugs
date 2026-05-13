package ui_tests.iteration2_manual_test_cases;

import api_tests.iteraion2_senior.BaseTestSenior;
import org.junit.jupiter.api.DisplayName;

public class ChangeUserNameTestCases extends BaseTestSenior {

    @DisplayName("Предусловие по созданию пользователя")
    public void preconditionActions() {
        /**
         1. Авторизоваться под УЗ admin и получить токен выполнив запрос
         curl 'http://localhost:4111/api/v1/auth/login' \
         -X 'POST' \
         -H 'Content-Type: application/json' \
         --data-raw $'{\n
         "username": "admin",\n
         "password": "admin"\n}'
         2. Создать нового пользователя выполнив запрос используя в запросе токен админа указав
         username = рандомное значение из [A-Za-z0-9]
         password = рандомное значение из [A-Za-z0-9%$]
         role = USER
         curl 'http://localhost:4111/api/v1/admin/users' \
         -X 'POST' \
         -H 'Authorization: Basic YWRtaW46YWRtaW4=' \
         -H 'Content-Type: application/json' \
         --data-raw $'{\n
         "username": "valueFromTemplate",\n
         "password": "valueFromTemplate",\n
         "role": "USER"\n}'
         */
    }

    @DisplayName("Позитивный тест: пользователь может изменить имя на другое валидное")
    public void userCanChangeHisNameWithValidData() {
        //Какие требования касаемо отображения Noname, если name is null? Может быть должен отображать username?

        //Точно ли кнопка 'Save Changes' должна быть активна, если поле ввода имени пустое?

        //На 10 шаге баг: после задания имени без рефреша не обновляется имя пользователя
        // отображаемое справа сверху профиля
        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         ОР: по центру экрана отображается 'Welcome, noname!' из-за того, что поле name is null
         ОР: справа сверху возле username отображается текущее значение name равное 'Noname'
         5. Нажать 'Noname'
         ОР: открылась новая страница на которой отображается название 'Edit Profile'
         Строка для ввода имени пустая
         Строка для ввода имени содержит тултип 'Enter new name'
         Кнопка 'Save Changes' активна
         6. В поле ввода вводим любое позитивное значение, которое состоит из двух слов исключительно
         из латинских букв
         7. Нажать 'Save Changes'
         ОР: отображается модальное окно с информацией: Name updated successfully!
         8. В модальном окне нажать 'Ок'
         ОР: в поле ввода отображается введённое значение
         На странице справа сверху отображается вместо 'Noname' заданное имя
         9. Выполнить рефреш
         ОР: На странице справа сверху отображается заданное имя
         10. Нажать 'Home' справа снизу
         ОР: по центру экрана отображается 'Welcome, new_name!', где вместо 'new_name' отображается
         ранее заданное имя
         */
    }


    @DisplayName("Негативный тест: проверка, что пользователь видит ошибку при попытке изменения имени на невалидное")
    public void userCannotChangeHisNameWithInvalidData() {
        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         ОР: по центру экрана отображается 'Welcome, noname!' из-за того, что поле name is null
         ОР: справа сверху возле username отображается текущее значение name равное 'Noname'
         5. Нажать 'Noname'
         ОР: открылась новая страница на которой отображается название 'Edit Profile'
         Строка для ввода имени пустая
         Строка для ввода имени содержит тултип 'Enter new name'
         Кнопка 'Save Changes' активна
         6. В поле ввода вводим негативное значение, например, 1 слово из латинских букв
         7. Нажать 'Save Changes'
         ОР: отображается модальное окно с информацией: Name must contain two words with letters only
         8. В модальном окне нажать 'Ок'
         ОР: в поле ввода отображается введённое значение
         На странице справа сверху отображается 'Noname'
         9. Выполнить рефреш
         ОР: На странице справа сверху отображается 'Noname'
         Поле ввода стало пустым
         10. Нажать 'Home' справа снизу
         ОР: по центру экрана отображается 'Welcome, noname!' из-за того, что поле name is null
         */
    }

    @DisplayName("Негативный тест: проверка, что пользователь видит ошибку при попытке изменения имени не заполнив поле ввода")
    public void userCannotChangeHisNameWithEmptyField() {
        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         ОР: по центру экрана отображается 'Welcome, noname!' из-за того, что поле name is null
         ОР: справа сверху возле username отображается текущее значение name равное 'Noname'
         5. Нажать 'Noname'
         ОР: открылась новая страница на которой отображается название 'Edit Profile'
         Строка для ввода имени пустая
         Строка для ввода имени содержит тултип 'Enter new name'
         Кнопка 'Save Changes' активна
         6. В поле ввода ничего не вводим
         7. Нажать 'Save Changes'
         ОР: отображается модальное окно с информацией: Please enter a valid name.
         8. В модальном окне нажать 'Ок'
         ОР: На странице справа сверху отображается 'Noname'
         9. Нажать 'Home' справа снизу
         ОР: по центру экрана отображается 'Welcome, noname!' из-за того, что поле name is null
         */
    }

}
