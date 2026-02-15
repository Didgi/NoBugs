package practice12_final.second_task;


public class UserValidator {
    private static boolean validationEnabled = true;

    public static void setIsValidationEnabled(boolean validationEnabled) {
        UserValidator.validationEnabled = validationEnabled;
    }

    public static boolean isValidationEnabled() {
        return validationEnabled;
    }

    public static boolean nameValidation(String name) throws InvalidUserException {
        if (name == null){
            throw new InvalidUserException("Передано пустое имя");
        }
        if (validationEnabled) {
            if (name.isEmpty() || !name.matches("^[A-ZА-Я].*")) {
                throw new InvalidUserException("Имя должно быть не пустым и начинаться с заглавной буквы");
            }
        }
        return true;
    }

    public static boolean ageValidation(int age) throws InvalidUserException {
        if (validationEnabled) {
            if (!(age >= 18 && age < 100)) {
                throw new InvalidUserException("Возраст должен быть в пределах от 18 до 100 лет");
            }
        }
        return true;
    }

    public static boolean emailValidation(String email) throws InvalidUserException {
        if (email == null){
            throw new InvalidUserException("Передан пустой email");
        }
        if (validationEnabled) {
            String emailRegExp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
            if (!email.matches(emailRegExp)) {
                throw new InvalidUserException("Email должен соответствовать стандартному формату " +
                        "электронной почты. Например: email@mail.ru");
            }
        }
        return true;
    }
}
