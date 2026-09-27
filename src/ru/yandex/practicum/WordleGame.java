package ru.yandex.practicum;

import java.util.HashSet;
import java.util.Set;

public class WordleGame {
    private final GameLogger logger;
    private final WordleDictionary dictionary;
    private final String secretWord;
    private int maxAttempts = 6;
    private final Set<String> usedWords;
    private final Set<Character> bannedChars = new HashSet<>();
    private final char[] exactChars = new char[5];
    private final Set<Character> presentChars = new HashSet<>();
    private final Set<String> givenHints = new HashSet<>();

    public WordleGame(WordleDictionary dictionary, GameLogger logger) {
        this.dictionary = dictionary;
        this.logger = logger;
        this.secretWord = dictionary.getRandomWord();
        this.usedWords = new HashSet<>();

        logger.log("Новая игра запущена. Загаданное слово: " + secretWord);
    }

    public String makeMove(String guess) throws WordNotFoundInDictionaryException, NoAttemptsLeftException {

        if (maxAttempts <= 0) {
            throw new NoAttemptsLeftException("Ход невозможен: у вас закончились попытки!");
        }

        String normalizedGuess = WordleDictionary.normalize(guess);

        if (normalizedGuess.isEmpty()) {
            throw new WordNotFoundInDictionaryException("Ввод не может быть пустым!");
        }

        for (int i = 0; i < normalizedGuess.length(); i++) {
            char c = normalizedGuess.charAt(i);
            if (c < 'а' || c > 'я') {
                throw new WordNotFoundInDictionaryException("Слово должно состоять строго из русских букв!");
            }
        }
        if (normalizedGuess.length() != 5 || !dictionary.contains(normalizedGuess)) {
            throw new WordNotFoundInDictionaryException("Такого пятибуквенного слова нет в словаре!");
        }

        if (isRepeat(normalizedGuess)) {
            return "Вы уже вводили это слово или использовали его как подсказку!";
        }

        recordMove(normalizedGuess);

        if (isVictory(normalizedGuess)) {
            return "+++++";
        }

        String resultCipher = WordleDictionary.checkWord(normalizedGuess, secretWord);

        for (int i = 0; i < resultCipher.length(); i++) {
            char ch = normalizedGuess.charAt(i);

            if (resultCipher.charAt(i) == '+') {
                exactChars[i] = ch;
            } else if (resultCipher.charAt(i) == '^') {
                presentChars.add(ch);
            } else if (resultCipher.charAt(i) == '-') {
                boolean isUsedElsewhere = false;
                for (int j = 0; j < resultCipher.length(); j++) {
                    if (normalizedGuess.charAt(j) == ch && (resultCipher.charAt(j) == '+' || resultCipher.charAt(j) == '^')) {
                        isUsedElsewhere = true;
                        break;
                    }
                }
                if (!isUsedElsewhere) {
                    bannedChars.add(ch);
                }
            }
        }

        return resultCipher;
    }

    private boolean isRepeat(String guess) {
        if (usedWords.contains(guess)) {
            logger.log("Игрок повторно ввёл слово: " + guess);
            return true;
        }
        return false;
    }

    private void recordMove(String guess) {
        usedWords.add(guess);
        maxAttempts--;
    }

    private boolean isVictory(String guess) {
        if (guess.equals(secretWord)) {
            logger.log("Игрок угадал слово! Оставшиеся попытки: " + maxAttempts);
            return true;
        }
        return false;
    }

    public String getHint() throws NoHintsAvailableException {
        if (maxAttempts == 6) {
            String randomWord = dictionary.getRandomWord();
            givenHints.add(randomWord);
            return randomWord;
        }

        int totalWordsInDictionary = dictionary.size();
        for (int k = 0; k < 1000; k++) {
            String candidate = dictionary.getRandomWord();

            if (usedWords.contains(candidate) || givenHints.contains(candidate)) {
                continue;
            }

            boolean fitsPerfect = true;

            for (int i = 0; i < candidate.length(); i++) {
                char ch = candidate.charAt(i);

                if (exactChars[i] != '\0' && exactChars[i] != ch) {
                    fitsPerfect = false;
                    break;
                }

                if (bannedChars.contains(ch)) {
                    fitsPerfect = false;
                    break;
                }
            }

            if (!fitsPerfect) {
                continue;
            }

            for (char presentChar : presentChars) {
                if (candidate.indexOf(presentChar) == -1) {
                    fitsPerfect = false;
                    break;
                }
            }

            if (fitsPerfect) {
                givenHints.add(candidate);
                logger.log("Компьютер выдал подсказку: " + candidate);
                return candidate;
            }
        }

        throw new NoHintsAvailableException("Компьютер не смог подобрать подсказку.");
    }

    public int getRemainingAttempts() {
        return maxAttempts;
    }

    public boolean isGameOver(String lastResult) {
        return maxAttempts <= 0 || "+++++".equals(lastResult);
    }

    public String getSecretWord() {
        return secretWord;
    }
}