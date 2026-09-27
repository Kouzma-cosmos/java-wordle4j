package ru.yandex.practicum;

import java.util.List;

public class WordleDictionary {

    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            return null;
        }
        int randomIndex = (int) (Math.random() * words.size());
        return words.get(randomIndex);
    }

    public int size() {
        return words.size();
    }

    public static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toLowerCase().replace('ё', 'е');
    }

    public static String checkWord(String guess, String secret) {
        int length = secret.length();

        char[] guessChars = new char[length];
        char[] secretChars = new char[length];
        String[] result = new String[length];

        for (int i = 0; i < length; i++) {
            guessChars[i] = guess.charAt(i);
            secretChars[i] = secret.charAt(i);
        }

        // Этап 1: Ищем только точные совпадения (+)
        for (int i = 0; i < length; i++) {
            if (guessChars[i] == secretChars[i]) {
                result[i] = "+";
                guessChars[i] = ' ';
                secretChars[i] = ' ';
            }
        }

        for (int i = 0; i < length; i++) {
            if (result[i] != null) {
                continue;
            }

            if (guessChars[i] == ' ') {
                continue;
            }

            boolean found = false;
            for (int j = 0; j < length; j++) {
                if (guessChars[i] == secretChars[j]) {
                    result[i] = "^";
                    secretChars[j] = ' ';
                    found = true;
                    break;
                }
            }

            if (!found) {
                result[i] = "-";
            }
        }

        StringBuilder finalResult = new StringBuilder();

        for (int i = 0; i < length; i++) {
            finalResult.append(result[i]);
        }

        return finalResult.toString();
    }

}
