package other_m;

public class Palindrome {
    public static void main(String[] args) {

//        System.out.println(isPalindrome("Me M e M"));
        System.out.println(isPalindromeNum(121));
    }

    public static boolean isPalindrome(String str){
        /**
         * 1. Обработать пустую строку
         * 2. Если не пустая, то убрать все не нужные символы
         * 3. Привести к нижнему регистру;
         * 4. Перевернуть полученную строку;
         * 5. Сравнить исходную и полученную строки
         */

        if (str == null) throw new IllegalArgumentException("Передана пустая строка");

        final String cleanedStr = str.replaceAll(" ", "");
        final String resultStr = new StringBuilder(cleanedStr).reverse().toString();
        return cleanedStr.equals(resultStr);
    }

    public static boolean isPalindromeNum(int num){
        /**
         * 1. Обработать пустую строку
         * 2. Если не пустая, то убрать все не нужные символы
         * 3. Привести к нижнему регистру;
         * 4. Перевернуть полученную строку;
         * 5. Сравнить исходную и полученную строки
         */

        Integer intNew = num;
        final String string = intNew.toString();
        StringBuilder strBu = new StringBuilder(string);
        final StringBuilder reverseNum = strBu.reverse();
        final int numNew = Integer.parseInt(reverseNum.toString());

        return num == numNew;

    }
}
