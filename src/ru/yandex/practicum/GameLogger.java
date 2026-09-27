package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class GameLogger implements AutoCloseable {
    private final PrintWriter writer;

    public GameLogger(String fileName) {
        try {
            this.writer = new PrintWriter(new FileWriter(fileName, StandardCharsets.UTF_8, true), true);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось открыть файл лога: " + fileName, e);
        }
    }

    public GameLogger(PrintWriter printWriter) {
        this.writer = printWriter;
    }

    public void log(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    @Override
    public void close() {
        if (writer != null) {
            writer.close();
        }
    }
}

