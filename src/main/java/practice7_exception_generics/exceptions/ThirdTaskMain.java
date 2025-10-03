package practice7_exception_generics.exceptions;

public class ThirdTaskMain {

    public static boolean checkAge(int age) throws InvalidAgeException {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Не подходящий возраст - " + age);
        } else {
            System.out.println("Возраст пользователя " + age + " - подходящий");
            return true;
        }
    }

    public static void main(String[] args) {

        try {
            System.out.println(checkAge(-1));
        } catch (Exception e) {
            System.out.println("Ошибка. " + e.getMessage());
        }

    }
}
