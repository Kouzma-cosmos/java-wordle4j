package ru.yandex.practicum;

import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        try (GameLogger logger = new GameLogger("wordle.log")) {
            logger.log("--- СТАРТ ПРОГРАММЫ ---");

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);

            WordleDictionary dictionary = loader.load("words_ru.txt");

            WordleGame game = new WordleGame(dictionary, logger);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Добро пожаловать в Wordle!");
            System.out.println("Компьютер загадал слово из 5 букв. У вас есть " + game.getRemainingAttempts()
                    + " " + "попыток.");
            System.out.println("Чтобы получить подсказку от компьютера, просто нажмите ENTER в пустой строке.");
            System.out.println("------------------------------------------------------------------");

            String lastResult = "";

            while (!game.isGameOver(lastResult)) {
                System.out.print("Ваш ход (осталось попыток " + game.getRemainingAttempts() + "): ");
                String input = scanner.nextLine();

                if (input.trim().isEmpty()) {
                    try {
                        String hint = game.getHint();
                        System.out.println("🤖 Подсказка компьютера: " + hint);
                    } catch (NoHintsAvailableException e) {
                        System.out.println("🤖 Компьютер разводит руками: " + e.getMessage());
                        logger.log("Предупреждение: Подсказка не найдена. " + e.getMessage());
                    }
                    continue;
                }


                try {
                    lastResult = game.makeMove(input);

                    System.out.println("Результат: " + lastResult);
                    System.out.println();

                } catch (WordNotFoundInDictionaryException e) {
                    System.out.println("❌ Ошибка ввода: " + e.getMessage());
                    logger.log("Игрок споткнулся об исключение: " + e.getMessage());
                }

            }

            if ("+++++".equals(lastResult)) {
                System.out.println("🎉 Поздравляем! Вы угадали слово: " + game.getSecretWord().toUpperCase());
                logger.log("Игра завершилась победой игрока.");
            } else {
                System.out.println("😢 Попытки закончились. Вы проиграли!");
                System.out.println("Загаданное слово было: " + game.getSecretWord().toUpperCase());
                logger.log("Игра завершилась проигрышем. Загаданное слово: " + game.getSecretWord());
            }

            logger.log("--- КОНЕЦ ПРОГРАММЫ ---");

        } catch (Exception e) {
            System.err.println("Критическая ошибка: " + e.getMessage());
        }
    }
}
