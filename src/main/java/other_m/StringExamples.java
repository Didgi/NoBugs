package other_m;

import java.util.*;

public class StringExamples {
    public static void main(String[] args) {
//        String example = "A man, a plan, a canal: Panama";
//        System.out.println(isPalindrome(example));
//        reverseWordsInStr("the sky is blue");
//        System.out.println(findMaxSeqNoDouble("abcabcdbb"));
//        System.out.println(getSumVowelsInStr("baebaa"));
//        System.out.println(isContainsVowels("ba"));
        System.out.println(isAnagram("cba", "abb"));

    }

    public static boolean isPalindrome(String str) {
        final String formattedStr = str.toLowerCase().replaceAll("[,: ]", "");
        StringBuilder strBld = new StringBuilder(formattedStr).reverse();
        System.out.println(str);
        System.out.println(strBld);

        return formattedStr.equals(strBld.toString());

    }

    //перевернуть слова в строке убрав пробелы в начале и конце. the sky is blue -> blue is sky the
    public static void reverseWordsInStr(String str) {
        final String[] strArray = str.trim().split(" ");

        for (int i = 0; i < strArray.length / 2; i++) {
            String temp = strArray[i];
            strArray[i] = strArray[strArray.length - i - 1];
            strArray[strArray.length - i - 1] = temp;
        }

        System.out.println(str);
        System.out.println(Arrays.toString(strArray));
    }

    //Найти длину самой длинной последовательности подстроки без повтора
    //abcabcbb -> 3

    public static int findMaxSeqNoDouble(String str) {
        int max = 0;
        int p1 = 0;
        Set<Character> symbols = new HashSet<>();
        for (int p2 = 0; p2 < str.length(); p2++) {
            char currentChar = str.charAt(p2);
            if (symbols.contains(currentChar)) {
                symbols.remove(str.charAt(p1));
                p1++;
            }
            symbols.add(currentChar);
//            max = Math.max(max, p2 - p1 + 1);
            if (max < p2 - p1 + 1) {
                max = p2 - p1 + 1;
            }
        }
        return max;
    }

    public static int getSumVowelsInStr(String str) {
        final String formattedStr = str.toLowerCase();
        String vowels = "aeoui";
        int count = 0;
        final char[] charArray = str.toLowerCase().toCharArray();

        for (char c : charArray) {
            if (vowels.indexOf(c) >= 0) {
                count++;
            }
        }
        return count;
    }

    public static boolean isContainsVowels(String str) {
        final String formattedStr = str.toLowerCase();
        String vowels = "aeoui";
        boolean result = false;
        final char[] charArray = str.toLowerCase().toCharArray();

        while (!result) {
            for (char c : charArray) {
                result = vowels.indexOf(c) >= 0;
            }
        }
        return result;
    }

    //анаграмма acaoz и caoza
    //анаграмма bac и abc

    public static boolean isAnagram(String str1, String str2){
        final char[] charArrayStr1 = str1.toCharArray();
        final char[] charArrayStr2 = str2.toCharArray();
        Arrays.sort(charArrayStr1);
        Arrays.sort(charArrayStr2);
        return Arrays.equals(charArrayStr1, charArrayStr2);
    }


}
