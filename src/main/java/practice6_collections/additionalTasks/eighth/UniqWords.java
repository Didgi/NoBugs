package practice6_collections.additionalTasks.eighth;

import java.util.HashSet;

public class UniqWords {

    private HashSet<String> uniqWords = new HashSet<>();

    public HashSet<String> getUniqWords(String text){
        final String[] splitWords = text.split("\\s+");
        for (String word:
             splitWords) {
            String cleanedWord = word.replaceAll("[^a-zA-Z]","").toLowerCase();
            if (!cleanedWord.isEmpty()){
                uniqWords.add(cleanedWord);
            }
            else {
                System.out.println("В тексте отсутствуют слова");
            }
        }
        return uniqWords;
    }

    public int getCountUniqWords(){
       return uniqWords.size();
    }

}
