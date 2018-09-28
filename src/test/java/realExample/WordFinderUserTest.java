package realExample;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WordFinderUserTest {
    private static final String FILE_ANY = "file://any";
    private static final String WORD = "";
    private WordFinderUser wordFinderUser;
    private WordFinder mock = Mockito.mock(WordFinder.class);
    private final static String FIRST_SENTENCE = "Doing Test";
    private final static String SECOND_SENTENCE = "Let's do it";

    // В этом тесте будет проверяться логика работы нашего класса WordFinderUser,
    // ипользующего в себе методы класса, реализующего интерфейс WordFinder.
    // При этом тестироваться будет работа только нашего класса, а WordFinder будет
    // подменен заглушкой, поскольку мы делаем тест в отрыве от WordFinder, логику
    // работы которого мы тестировать не можем. Нам важны лишь результаты работы его
    // методов в нашем классе, поэтому эти результаты можно просто сымитировать.
    // Мы просто проверим, как будет работать наша программа если мок(имитация)
    //вернет то или иное значение, и будут ли вызываться методы заглушки, в зависимости
    // от того что мы делаем у себя в классе

    // src\main\java\realExample\WordFinderUser.java



    @BeforeEach
    void beforeEach() {
        // тестируемому классу подкладываем не реальный объект класса,
        // реализующего интерфейс WordFinder, а mock - заглушку.
        wordFinderUser = new WordFinderUser(mock);
    }

    @Test
    void doWordNullTest() {
        // в этом тесте мы хотим проверить, что если метод мока вернет null,
        // то это не обрушит нашу программу.

        // говорим что метод заглушки специально должнен вернуть null, если
        // при любом (any) аргументе
        when(mock.getSentence(any())).thenReturn(null);
        // проверяем что при возвращаемом null в методе не было брошено никаких исключений
        assertDoesNotThrow(()-> wordFinderUser.doWord(FILE_ANY, WORD) );
    }

    @Test
    void doWordEmptySetTest()  {
        // проверям что если мок вернул пустой сет, то не будут вызваться последующие
        // методы, использующие данные из этого сета

        // возвращаем пустой сет
        when(mock.getSentence(any())).thenReturn(Collections.emptySet());
        try {
            // вызвааем тестиремый метод
            wordFinderUser.doWord(FILE_ANY, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // провереряем, что методы не были вызваны ни разу
        verify(mock, times(0)).checkIfWordInSentence(any(),any());
        verify(mock, times(0)).writeSentenceToResult(any());
    }


    @Test
    void doWordCorrectCallWriteMethodTest() {
        // этот метод проверяет, что элементы сете, для которых метод checkIfWordInSentence
        // верент true, будут записаны в методом writeSentenceToResult, и не будут записаны,
        // если checkIfWordInSentence вернет false

        // создаем сет
        when(mock.getSentence(any())).thenReturn(new HashSet<>(Arrays.asList(FIRST_SENTENCE, SECOND_SENTENCE)));

        // имитируем вызов методов с соответствующими аргументами
        when(mock.checkIfWordInSentence(FIRST_SENTENCE, WORD)).thenReturn(true);
        when(mock.checkIfWordInSentence(SECOND_SENTENCE, WORD)).thenReturn(false);
        try {
            // вызов тестируемого метода
            wordFinderUser.doWord(FILE_ANY, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // для первого предложения метод должен вызваться, для второго нет
        verify(mock, times(1)).writeSentenceToResult(FIRST_SENTENCE);
        verify(mock, times(0)).writeSentenceToResult(SECOND_SENTENCE);
    }

    @Test
    void doWordCorrectURLForming() {
        // необходимо убедиться что в методе wordFinder.getSentence, используемом в тестируемом методе,
        // URL использует имеено тот ресурс, который передан аргументом в тестируемом методе в неизменном виде,
        // например если в строке url = new URL(resource) метода doWord() изменить ресурс на другой,
        // то этот тест не будет пройден


        // создаем имитацию аргумента
        final ArgumentCaptor<URL> argument = ArgumentCaptor.forClass(URL.class);
        try {
            // запускаем метод (FILE_ANY это и есть аргумент с ресурсом)
            wordFinderUser.doWord(FILE_ANY, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // вызываем метод, использующий url с ресурсом
        verify(mock).getSentence(argument.capture());
        // проверяем одинаковы ли ресурсы
        assertEquals(FILE_ANY, argument.getValue().toString());
    }

    @Test
    void doWordBadURL() {
        // проверяем будет ли в брошено исключение, если в тестируемый метод будет передан
        // некорректный ресурс (неправильно обозначенный путь и т.д.)

        assertThrows(MalformedURLException.class, () -> wordFinderUser.doWord("d;/path", ""));
    }
}