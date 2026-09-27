package ru.yandex.practicum;

// Специализированное исключение для ситуации, когда у компьютера кончились подсказки
public class NoHintsAvailableException extends Exception {
    public NoHintsAvailableException(String message) {
        super(message);
    }
}
