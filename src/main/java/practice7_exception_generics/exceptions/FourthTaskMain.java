package practice7_exception_generics.exceptions;

public class FourthTaskMain {

    /*
    Напишите функцию, которая принимает строку в качестве аргумента и проверяет,
    является ли строка правильным электронным адресом. Если строка не удовлетворяет критериям,
    функция должна выбрасывать непроверяемое исключение.
     */

    public static boolean checkEmail(String email) {
        if (!(email.endsWith("ru") || email.endsWith("com"))) {
            throw new InvalidEmailException("Не верный формат домена в email " + email);
        }
        if (!email.contains("@")) {
            throw new InvalidEmailException("Собака убежала из email " + email);
        }
        String regExpFormat = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!email.matches(regExpFormat)) {
            throw new InvalidEmailException("Не верный формат email " + email);
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println(checkEmail("test@test.com"));
        System.out.println(checkEmail("test@test.c"));
        System.out.println(checkEmail("testtest.com"));
        System.out.println(checkEmail("@test.com"));
    }
}
