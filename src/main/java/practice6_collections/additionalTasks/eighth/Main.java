package practice6_collections.additionalTasks.eighth;

public class Main {
    public static void main(String[] args) {
        UniqWords uniqWords = new UniqWords();
        System.out.println("Уникальные слова в тексте: " + uniqWords.getUniqWords("Hello, NoBugs. How are you? Nobugs?"));
        System.out.println("Количество уникальных слов в тексте: " + uniqWords.getCountUniqWords());
        System.out.println("Уникальные слова в тексте: " + uniqWords.getUniqWords("...?"));
    }
}
