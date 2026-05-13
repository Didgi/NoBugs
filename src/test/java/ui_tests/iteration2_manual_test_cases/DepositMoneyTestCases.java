package ui_tests.iteration2_manual_test_cases;

import org.junit.jupiter.api.DisplayName;

public class DepositMoneyTestCases {

    @DisplayName("Предусловие по созданию пользователя и его аккаунта")
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
         3. Авторизоваться под созданным пользователем и получить токен выполнив запрос
         curl 'http://localhost:4111/api/v1/auth/login' \
         -X 'POST' \
         -H 'Content-Type: application/json' \
         --data-raw $'{\n
         "username": "valueFromPreviewRequest",\n
         "password": "valueFromPreviewRequest"\n}'
         4. Создать аккаунт пользователю выполнив запрос передав токен пользователя
         curl 'http://localhost:4111/api/v1/accounts' \
         -X 'POST' \
         -H 'Authorization: Basic YWRtaW46YWRtaW4=' \
         */
    }

    @DisplayName("Позитивный тест: пользователь пополняет свой аккаунт валидной суммой")
    public void userCanDepositHisAccount() {
        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         5. Нажать 'Deposit Money'
         ОР: открылась новая страница на которой отображается название 'Deposit Money'
         6. Открываем выпадающий список 'Select Account'
         ОР: В выпадающем списке отображается наименование единственного созданного аккаунта в формате:
         ACC763 (Balance: $0.00), где 763 - id аккаунта в системе
         7. Выбираем единственный аккаунт
         8. В поле 'Enter Amount' ввести рандомное значение в диапазоне 0.01-5000
         ОР: в поле указана введённая сумма
         9. Нажать 'Deposit'
         ОР: отображается модальное окно с информацией: Successfully deposited $0.01 to account ACC763!
         10. В модальном окне нажать 'Ок'
         ОР: произошёл переход в главное меню на котором есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         11. Проверяем, что пополнение аккаунта выполнено успешно вызвав api ручку передав токен пользователя,
         где в ответе проверяем, что в поле account.balance отображается верное значение для
         верного account.accountNumber
         curl 'http://localhost:4111/api/v1/customer/profile' \
         -H 'Authorization: Basic YWxleDpBbGV4QWxleDEh' \
         */
    }

    @DisplayName("Позитивный тест: пользователь может положить деньги на свои любые аккаунты")
    public void userCanDepositMoneyIntoHisDiffAccounts() {
        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         4. Нажать 'Create New Account'
         ОР: В выпадающем списке отображается имя второго созданного аккаунта в формате:
         New Account Created! Account Number: ACC765, где 765 - id аккаунта в системе
         5. Нажать 'Deposit Money'
         ОР: открылась новая страница на которой отображается название 'Deposit Money'
         6. Открываем выпадающий список 'Select Account'
         ОР: В выпадающем списке отображаются 2 созданных аккаунта
         7. Выбираем первый аккаунт
         8. В поле 'Enter Amount' ввести рандомное значение в диапазоне 0.01-5000
         ОР: в поле указана введённая сумма
         9. Нажать 'Deposit'
         ОР: отображается модальное окно с информацией: Successfully deposited $0.01 to account ACC763!,
         где указан id первого аккаунта
         10. В модальном окне нажать 'Ок'
         ОР: произошёл переход в главное меню на котором есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         11. Вновь нажать 'Deposit Money'
         ОР: открылась новая страница на которой отображается название 'Deposit Money'
         12. Открываем выпадающий список 'Select Account' и выбираем 2й аккаунт
         13. В поле 'Enter Amount' ввести рандомное значение в диапазоне 0.01-5000
         ОР: в поле указана введённая сумма
         14. Нажать 'Deposit'
         ОР: отображается модальное окно с информацией: Successfully deposited $0.01 to account ACC763!,
         где указан id второго аккаунта
         15. В модальном окне нажать 'Ок'
         ОР: произошёл переход в главное меню на котором есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         16. Проверяем, что пополнение первого аккаунта выполнено успешно вызвав api ручку передав токен пользователя,
         где в ответе проверяем, что в поле account.balance отображается верное значение для
         первого account.accountNumber
         curl 'http://localhost:4111/api/v1/customer/profile' \
         -H 'Authorization: Basic YWxleDpBbGV4QWxleDEh' \
        17. Проверяем, что пополнение первого аккаунта выполнено успешно вызвав api ручку передав токен пользователя,
        где в ответе проверяем, что в поле account.balance отображается верное значение для
        второго account.accountNumber
        curl 'http://localhost:4111/api/v1/customer/profile' \
        -H 'Authorization: Basic YWxleDpBbGV4QWxleDEh' \
         */

    }

    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой меньше 0.01")
    public void userSeesErrorMessageWhenDepositHisAccountWithLessThanMiniumLimitValue() {

        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         5. Нажать 'Deposit Money'
         ОР: открылась новая страница на которой отображается название 'Deposit Money'
         6. Открываем выпадающий список 'Select Account'
         ОР: В выпадающем списке отображается наименование единственного созданного аккаунта в формате:
         ACC763 (Balance: $0.00), где 763 - id аккаунта в системе
         7. Выбираем единственный аккаунт
         8. В поле 'Enter Amount' ввести рандомное значение равное -0.01
         ОР: в поле указана введённая сумма
         9. Нажать 'Deposit'
         ОР: отображается модальное окно с информацией 'Please enter a valid amount.'
         10. В модальном окне нажать 'Ок'
         ОР: модальное окно с информацией 'Please enter a valid amount.' закрылось
         ОР: отображается прежняя страница на которой отображается название 'Deposit Money'
         11. Проверяем, что пополнение аккаунта не выполнено вызвав api ручку передав токен пользователя,
         где в ответе проверяем, что в поле account.balance отображается значение = 0 для
         верного account.accountNumber
         curl 'http://localhost:4111/api/v1/customer/profile' \
         -H 'Authorization: Basic YWxleDpBbGV4QWxleDEh' \
         */

    }

    @DisplayName("Негативный тест: проверка отображения ошибки при попытке пополнить свой аккаунт суммой больше 5000")
    public void userSeesErrorMessageWhenDepositHisAccountWithValueMoreThanMaximum5000() {

        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         5. Нажать 'Deposit Money'
         ОР: открылась новая страница на которой отображается название 'Deposit Money'
         6. Открываем выпадающий список 'Select Account'
         ОР: В выпадающем списке отображается наименование единственного созданного аккаунта в формате:
         ACC763 (Balance: $0.00), где 763 - id аккаунта в системе
         7. Выбираем единственный аккаунт
         8. В поле 'Enter Amount' ввести рандомное значение равное 5000.01
         ОР: в поле указана введённая сумма
         9. Нажать 'Deposit'
         ОР: отображается модальное окно с информацией 'Please deposit less or equal to 5000$.'
         10. В модальном окне нажать 'Ок'
         ОР: модальное окно с информацией 'Please deposit less or equal to 5000$.' закрылось
         ОР: отображается прежняя страница на которой отображается название 'Deposit Money'
         11. Проверяем, что пополнение аккаунта не выполнено вызвав api ручку передав токен пользователя,
         где в ответе проверяем, что в поле account.balance отображается значение = 0 для
         верного account.accountNumber
         curl 'http://localhost:4111/api/v1/customer/profile' \
         -H 'Authorization: Basic YWxleDpBbGV4QWxleDEh' \
         */

    }

    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без выбора аккаунта")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAccount() {

        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         5. Нажать 'Deposit Money'
         ОР: открылась новая страница на которой отображается название 'Deposit Money'
         6. Не открываем выпадающий список 'Select Account' и не выбираем никакой аккаунт
         7. В поле 'Enter Amount' ничего не вводим
         9. Нажать 'Deposit'
         ОР: отображается модальное окно с информацией 'Please select an account.'
         10. В модальном окне нажать 'Ок'
         ОР: отображается прежняя страница на которой отображается название 'Deposit Money'
         */

    }

    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' без указания суммы")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAmount() {

        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         5. Нажать 'Deposit Money'
         ОР: открылась новая страница на которой отображается название 'Deposit Money'
         6. Открываем выпадающий список 'Select Account'
         ОР: В выпадающем списке отображается наименование единственного созданного аккаунта в формате:
         ACC763 (Balance: $0.00), где 763 - id аккаунта в системе
         7. Выбираем единственный аккаунт
         8. В поле 'Enter Amount' ничего не вводим
         9. Нажать 'Deposit'
         ОР: отображается модальное окно с информацией 'Please enter a valid amount.'
         10. В модальном окне нажать 'Ок'
         ОР: отображается прежняя страница на которой отображается название 'Deposit Money'
         11. Проверяем, что никаких транзакций по аккаунта не выполнено вызвав api ручку передав токен пользователя,
         где в ответе проверяем, что для верного account.accountNumber массив account.transactions - пуст
         curl 'http://localhost:4111/api/v1/customer/profile' \
         -H 'Authorization: Basic YWxleDpBbGV4QWxleDEh' \
         */

    }

    @DisplayName("Негативный тест: проверка отображения ошибки при попытке нажатия 'Deposit' с пустым аккаунтом, " +
            "хотя ранее он был выбран")
    public void userSeesErrorMessageWhenClickDepositButtonWithoutAccountWhenAccountWasChooseBefore() {

        /**
         1. Открыть страницу http://localhost:3000/
         2. В поле username ввести имя созданного пользователя
         3. В поле password ввести пароль созданного пользователя
         4. Нажать 'login'
         ОР: открылась новая страница на которой есть следующие элементы:
         - кнопка Deposit Money
         - кнопка Make a transfer
         - кнопка Create New Account
         5. Нажать 'Deposit Money'
         ОР: открылась новая страница на которой отображается название 'Deposit Money'
         6. Открываем выпадающий список 'Select Account'
         ОР: В выпадающем списке отображается наименование единственного созданного аккаунта в формате:
         ACC763 (Balance: $0.00), где 763 - id аккаунта в системе
         7. Выбираем единственный аккаунт
         8. Вновь открываем выпадающий список 'Select Account' и выбираем '-- Choose an account --'
         ОР: выпадающий список закрылся.
         ОР: выбрано значение '-- Choose an account --'
         9. В поле 'Enter Amount' ввести рандомное значение в диапазоне 0.01-5000
         10. Нажать 'Deposit'
         ОР: отображается модальное окно с информацией 'Please select an account.'
         10. В модальном окне нажать 'Ок'
         ОР: отображается прежняя страница на которой отображается название 'Deposit Money'
         11. Проверяем, что никаких транзакций по ранее выбранному аккаунту не выполнено вызвав api ручку передав токен пользователя,
         где в ответе проверяем, что для верного account.accountNumber массив account.transactions - пуст
         curl 'http://localhost:4111/api/v1/customer/profile' \
         -H 'Authorization: Basic YWxleDpBbGV4QWxleDEh' \
         */

    }

}
