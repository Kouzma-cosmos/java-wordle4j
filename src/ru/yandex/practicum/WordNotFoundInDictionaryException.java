package ru.yandex.practicum;

// Наше собственное исключение для игровых ситуаций
public class WordNotFoundInDictionaryException extends Exception {
    public WordNotFoundInDictionaryException(String message) {
        super(message);
    }
}
