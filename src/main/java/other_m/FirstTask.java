package other_m;

import java.util.*;

public class FirstTask {

    public static void main(String[] args) {
//        String first = "Banana apple java";
//        String second = "banana mango";
//        String third = "banana mango";
//        uniqueWords(third, second);
        Boolean b = new Boolean("true");
        System.out.println(b);
    }

    public static void uniqueWords(String firstWord, String secondWord) {
        String common = (firstWord + " " + secondWord);
        final String[] commonArray = common.toLowerCase().split(" ");
//        final String[] secondArray = secondWord.toLowerCase().split(" ");
        Set<String> uniqueSet = new HashSet<>();

        for (String i : commonArray) {
//            for (String j : secondArray) {
//                if (!uniqueSet.add(j)) {
//                    uniqueSet.remove(j);
//                }
//            }
            if (!uniqueSet.add(i)) {
                uniqueSet.remove(i);
            }
        }


        System.out.println(new ArrayList<>(uniqueSet));
    }

    public static class A{

    };
    public static class B{

    };


}
