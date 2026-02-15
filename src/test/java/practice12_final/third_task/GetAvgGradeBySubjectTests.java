package practice12_final.third_task;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetAvgGradeBySubjectTests extends BaseTest {

    /**
     * Позитивные тесты:
     * Получение средней оценки >0 по любому предмету, когда есть добавленные оценки -> success
     * <p>
     * Негативные тесты:
     * Получение средней оценки = 0 по предмету для которого ранее не было добавленных оценок -> success
     */


    //Позитивный тест
    @Test
    @DisplayName("Проверка на то, что успешно рассчитывается средняя оценка по предмету по которому есть добавленные оценки")
    public void getAvgGradeIsSuccessWhenGradeListHasSeveralGradesForSubject() throws InvalidGradeException, InvalidStudentNameException {
        Subjects subject = Subjects.INFORMATICS;
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            String randomName = RandomStringUtils.secure().nextAlphabetic(5);
            int randomGrade = new Random().nextInt(6);
            sum += randomGrade;
            gradeService.addGrade(new StudentGrade<>(randomName, subject, randomGrade));
        }
        double actualAvgGrade = gradeService.getAvgGradeBySubject(subject);
        assertEquals((double) sum / 3, actualAvgGrade);
    }

    //Негативный тест
    @Test
    @DisplayName("Проверка на то, что успешно рассчитывается средняя оценка по предмету по которому отсутствуют добавленные оценки")
    public void getAvgGradeIsSuccessWhenGradeListForSubjectIsEmpty() throws InvalidGradeException, InvalidStudentNameException {
        String randomName = RandomStringUtils.secure().nextAlphabetic(5);
        int randomGrade = new Random().nextInt(6);
        gradeService.addGrade(new StudentGrade<>(randomName, Subjects.PHYSICS, randomGrade));
        double actualAvgGrade = gradeService.getAvgGradeBySubject(Subjects.INFORMATICS);
        assertEquals(0.0, actualAvgGrade);
    }

}
