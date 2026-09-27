package ru.yandex.practicum;

public class NoAttemptsLeftException extends Exception {
    public NoAttemptsLeftException(String message) {
        super(message);
    }
}
