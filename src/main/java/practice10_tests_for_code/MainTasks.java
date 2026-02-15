package practice10_tests_for_code;

import java.util.Arrays;
import java.util.Comparator;

public class MainTasks {
    //firstTask
    public boolean isEven(int number) {
        return number % 2 == 0;
    }
    //secondTask
    public int countVowels(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        return (int) input.toLowerCase().chars()
                .filter(c -> "aeiou".indexOf(c) != -1)
                .count();
    }
    //thirdTask
    public String reverse(String input) {
        if (input == null) return null;
        return new StringBuilder(input).reverse().toString();
    }
    //fourthTask
    public int findMax(int[] numbers) {
        return Arrays.stream(numbers).max().orElseThrow();
    }
    //fifthTask
    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    //sixthTask
    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }
    //seventhTask
    public int factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Negative numbers not allowed");
        return (n == 0) ? 1 : n * factorial(n - 1);
    }
    //eighthTask
    public int findSecondMax(int[] numbers) {
        //Изначальный код
//        return Arrays.stream(numbers).distinct().sorted().skip(numbers.length - 2).findFirst().orElseThrow();
        //Исправленный код
        return Arrays.stream(numbers).distinct().boxed().sorted(Comparator.reverseOrder()).skip(1)
                .findFirst().orElseThrow();
    }
    //ninthTask
    public int countWords(String sentence) {
        return sentence.trim().isEmpty() ? 0 : sentence.split("\\s+").length;
    }
    //tenthTask
    public boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\+\\d{1,3} \\d{10}");
    }
}
