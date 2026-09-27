package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    private final GameLogger logger;

    public WordleDictionaryLoader(GameLogger logger) {
        this.logger = logger;
    }

    public WordleDictionary load(String fileName) throws EmptyDictionaryException {
        logger.log("Запуск загрузки словаря из файла: " + fileName);

        List<String> filteredWords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;

            while ((line = br.readLine()) != null) {
                String normalizedWord = WordleDictionary.normalize(line);

                if (normalizedWord.length() == 5) {
                    filteredWords.add(normalizedWord);
                }
            }
            if (filteredWords.isEmpty()) {
                throw new EmptyDictionaryException("Файл словаря не содержит подходящих пятибуквенных слов!");
            }

            logger.log("Загрузка завершена успешно. Найдено подходящих слов: " + filteredWords.size());

        } catch (IOException e) {
            logger.log("КРИТИЧЕСКАЯ ОШИБКА: Не удалось прочитать файл словаря! " + e.getMessage());
            throw new RuntimeException("Ошибка при загрузке словаря", e);
        }
        return new WordleDictionary(filteredWords);
    }
}
