package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private static GameLogger testLogger;
    private WordleDictionary testDictionary;
    private WordleGame game;

    @BeforeAll
    static void initAll() {
        PrintWriter testWriter = new PrintWriter(System.out, true);
        testLogger = new GameLogger(testWriter);
    }

    @BeforeEach
    void setUp() {
        List<String> mockWords = new ArrayList<>();
        mockWords.add("поток");
        mockWords.add("капок");
        mockWords.add("лапка");
        mockWords.add("птица");
        mockWords.add("город");
        mockWords.add("канал");
        mockWords.add("океан");
        testDictionary = new WordleDictionary(mockWords);
        game = new WordleGame(testDictionary, testLogger);
    }

    @Test
    void shouldValidateInvalidInput() {
        assertThrows(WordNotFoundInDictionaryException.class, () -> game.makeMove("apple"));
        assertThrows(WordNotFoundInDictionaryException.class, () -> game.makeMove(""));
        assertThrows(WordNotFoundInDictionaryException.class, () -> game.makeMove("шкафы"));
    }

    @Test
    void shouldDecreaseAttemptsOnValidMove() throws Exception {
        int initialAttempts = game.getRemainingAttempts();
        game.makeMove("капок");
        assertEquals(initialAttempts - 1, game.getRemainingAttempts());
    }

    @Test
    void shouldNotDecreaseAttemptsOnRepeatedMove() throws Exception {
        int initialAttempts = game.getRemainingAttempts();
        game.makeMove("капок");
        String result = game.makeMove("капок");
        assertEquals(initialAttempts - 1, game.getRemainingAttempts());
        assertTrue(result.contains("Вы уже вводили это слово"));
    }

    @Test
    void shouldFailWhenNoAttemptsLeft() throws Exception {
        game.makeMove("капок");
        game.makeMove("лапка");
        game.makeMove("птица");
        game.makeMove("город");
        game.makeMove("канал");
        game.makeMove("океан");
        assertEquals(0, game.getRemainingAttempts());
        assertThrows(NoAttemptsLeftException.class, () -> game.makeMove("поток"));
    }

    @Test
    void shouldReturnCorrectHintAndAllowUsingIt() throws Exception {
        String firstHint = game.getHint();
        assertNotNull(firstHint);
        assertEquals(5, firstHint.length());
        assertDoesNotThrow(() -> game.makeMove(firstHint));
    }

    @Test
    void shouldGenerateSmartHintBasedOnBannedLetters() throws Exception {
        List<String> strictWords = new ArrayList<>();
        strictWords.add("поток");
        strictWords.add("лапка");
        WordleDictionary strictDictionary = new WordleDictionary(strictWords);
        WordleGame smartGame = new WordleGame(strictDictionary, testLogger);

        smartGame.makeMove("лапка");
        String smartHint = smartGame.getHint();

        assertNotEquals("лапка", smartHint);
        assertEquals("поток", smartHint);
    }
}

class WordleDictionaryTest {

    @Test
    void shouldNormalizeWordToLowerCaseAndReplaceYo() {
        assertEquals("метла", WordleDictionary.normalize(" Мётла "));
        assertEquals("арбуз", WordleDictionary.normalize("АРБУЗ"));
        assertEquals("", WordleDictionary.normalize(null));
    }

    @Test
    void shouldCorrectlyCipherWords() {
        String secret = "поток";
        assertEquals("+++++", WordleDictionary.checkWord("поток", secret));
        assertEquals("--^++", WordleDictionary.checkWord("капок", secret));
        assertEquals("-----", WordleDictionary.checkWord("шалаш", secret));
    }
}

class GameLoggerTest {

    @Test
    void shouldLogMessageCorrectly() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        GameLogger logger = new GameLogger(printWriter);

        logger.log("Тест записи");
        assertEquals("Тест записи", stringWriter.toString().trim());
    }
}
