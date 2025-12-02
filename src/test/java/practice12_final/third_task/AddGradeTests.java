package practice12_final.third_task;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddGradeTests extends BaseTest {
    /**
     * Позитивные:
     * Добавление целочисленной оценки от 0 до 5 по одному предмету -> success
     * Добавление дробной оценки от 0 до 5 по одному предмету -> success
     * <p>
     * Негативные:
     * Добавление целочисленной отрицательной или превышающей 5 оценки по одному предмету -> exception
     * Добавление валидной оценки по одному предмету с пустым именем студента -> exception
     * <p>
     * Потокобезопасность: добавление оценок по ученикам в 2х потоках -> success
     */

    String randomName = RandomStringUtils.secure().nextAlphabetic(5);

    //Позитивный тест на добавление целочисленной оценки
    @ParameterizedTest
    @ValueSource(ints = {
            0,
            5
    })
    @DisplayName("Добавление целочисленной оценки от 0 до 5 по одному предмету")
    public void addGradeIsSuccessWhenGradeIsIntAndValid(int grade) throws InvalidGradeException, InvalidStudentNameException {
        StudentGrade<Integer> studentGrade = new StudentGrade<>(randomName, Subjects.MATHS, grade);

        int expectedGradeList = gradeService.getStudentGradeList().size();
        assertEquals(0, expectedGradeList);
        gradeService.addGrade(studentGrade);

        int actualGradeList = gradeService.getStudentGradeList().size();
        assertEquals(expectedGradeList + 1, actualGradeList);

        final StudentGrade<Integer> studentGradeInList = gradeService.getStudentGradeList().get(0);
        assertEquals(randomName, studentGradeInList.getStudentName());
        assertEquals(Subjects.MATHS, studentGradeInList.getSubject());
        assertEquals(grade, studentGradeInList.getGrade());
    }

    //Позитивный тест на добавление дробной оценки
    @ParameterizedTest
    @ValueSource(doubles = {
            0.01,
            4.99
    })
    @DisplayName("Добавление дробной оценки от 0 до 5 по одному предмету")
    public void addGradeIsSuccessWhenGradeIsDoubleAndValid(double grade) throws InvalidGradeException, InvalidStudentNameException {
        StudentGrade<Double> studentGrade = new StudentGrade<>(randomName, Subjects.PHYSICS, grade);

        int expectedGradeList = gradeService.getStudentGradeList().size();
        assertEquals(0, expectedGradeList);
        gradeServiceDouble.addGrade(studentGrade);

        int actualGradeList = gradeService.getStudentGradeList().size();
        assertEquals(expectedGradeList + 1, actualGradeList);

        final StudentGrade<Double> studentGradeInList = gradeServiceDouble.getStudentGradeList().get(0);
        assertEquals(randomName, studentGradeInList.getStudentName());
        assertEquals(Subjects.PHYSICS, studentGradeInList.getSubject());
        assertEquals(grade, studentGradeInList.getGrade());
    }

    //Негативный тест на добавление целочисленной отрицательной оценки
    @ParameterizedTest
    @ValueSource(ints = {
            -1,
            6
    })
    @DisplayName("Добавление целочисленной отрицательной оценки -1 и положительной 6 по одному предмету")
    public void addGradeThrowsExceptionWhenGradeIsIntAndInvalid(int grade) throws InvalidGradeException, InvalidStudentNameException {
        StudentGrade<Integer> studentGrade = new StudentGrade<>(randomName, Subjects.MATHS, grade);

        int expectedGradeList = gradeService.getStudentGradeList().size();
        assertEquals(0, expectedGradeList);

        assertThrows(InvalidGradeException.class, () -> {
            gradeService.addGrade(studentGrade);
        });

        int actualGradeList = gradeService.getStudentGradeList().size();
        assertEquals(expectedGradeList, actualGradeList);
    }

    //Негативный тест
    @Test
    @DisplayName("Добавление валидной оценки с пустым именем студента")
    public void addGradeThrowsExceptionWhenStudentNameIsEmpty() throws InvalidGradeException, InvalidStudentNameException {
        StudentGrade<Integer> studentGrade = new StudentGrade<>("", Subjects.MATHS, 5);

        int expectedGradeList = gradeService.getStudentGradeList().size();
        assertEquals(0, expectedGradeList);

        assertThrows(InvalidStudentNameException.class, () -> {
            gradeService.addGrade(studentGrade);
        });

        int actualGradeList = gradeService.getStudentGradeList().size();
        assertEquals(expectedGradeList, actualGradeList);
    }

    //Тест на потокобезопасность
    @RepeatedTest(10)
    @DisplayName("Проверка на то, что оценки успешно добавляются при многопоточных вызовах метода addGrade")
    public void addGradeIsSuccessWhenDiffThreads() {

        Runnable taskOne = () -> {
            String localRandomName = RandomStringUtils.secure().nextAlphabetic(5);
            int localRandomGrade = new Random().nextInt(6);
            for (int i = 0; i < 1000; i++) {
                try {
                    gradeService.addGrade(new StudentGrade<>("1 " + localRandomName, Subjects.INFORMATICS, localRandomGrade));
                    Thread.sleep(5);
                } catch (InvalidGradeException | InvalidStudentNameException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Runnable taskTwo = () -> {
            String localRandomName = RandomStringUtils.secure().nextAlphabetic(5);
            int localRandomGrade = new Random().nextInt(6);
            for (int i = 0; i < 1000; i++) {
                try {
                    gradeService.addGrade(new StudentGrade<>("2 " + localRandomName, Subjects.INFORMATICS, localRandomGrade));
                    Thread.sleep(2);
                } catch (InvalidGradeException | InvalidStudentNameException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(taskOne);
        executorService.execute(taskTwo);
        executorService.shutdown();

        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            executorService.shutdownNow();
        }
        int actualListSize = gradeService.getStudentGradeList().size();

        assertEquals(2000, actualListSize);

    }

}
