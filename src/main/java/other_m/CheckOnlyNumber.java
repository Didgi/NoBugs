package other_m;

public class CheckOnlyNumber {

    public static void main(String[] args) {
        System.out.println(isContainsOnlyNumber("123 h"));
        System.out.println(isContainsOnlyNumber("As d h"));
        System.out.println(isContainsOnlyNumber(""));
        System.out.println(isContainsOnlyNumber(""));
        System.out.println(isContainsOnlyNumber(null));
        System.out.println(isContainsOnlyNumber("124512"));
        System.out.println(isContainsOnlyNumber("124512!"));

    }

    public static boolean isContainsOnlyNumber(String str){
        if (str == null || str.isEmpty()){
            return false;
        }

        final StringBuilder stringBuilder = new StringBuilder(str.replaceAll("[A-Za-z\\s+!]", ""));
        System.out.println(stringBuilder);

        return str.length() == stringBuilder.toString().length();

    }
}
