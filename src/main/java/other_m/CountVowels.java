package other_m;

public class CountVowels {
    public static void main(String[] args) {
        String vowels = new String("Hello, my Darling"); //4
        String vowels2 = new String("Hll, my Drlng"); //4
        System.out.println(getVowelsInString(vowels2));
    }

    public static int getVowelsInString(String str) {
        int count = 0;
        String expectedVowels = "aeiuo";
        final char[] charArray = str.toLowerCase().replaceAll("[^A-Za-z]", "").toCharArray();
        for (char c : charArray) {
            if (expectedVowels.indexOf(c) >= 0)
            {
                count++;
            }
        }
        return count;
    }
}
