package practice12_final.third_task;

import java.util.ArrayList;
import java.util.List;

public class GradeService<T extends Number> {
    /**
     * Список List<StudentGrade<T>> для хранения оценок.
     * Метод для добавления оценки (addGrade), который также валидирует оценку на предмет того, что она не отрицательна.
     * Метод для расчёта среднего значения оценок по конкретному предмету.
     * Обработка исключений через InvalidGradeException, если оценка некорректна.
     */

    private List<StudentGrade<T>> studentGradeList;

    public GradeService() {
        studentGradeList = new ArrayList<>();
    }

    public List<StudentGrade<T>> getStudentGradeList() {
        return studentGradeList;
    }

    public synchronized void addGrade(StudentGrade<T> studentGrade) throws InvalidGradeException, InvalidStudentNameException {
        if (studentGrade.getGrade().doubleValue() < 0) {
            throw new InvalidGradeException("Передана отрицательная оценка");
        }
        if (studentGrade.getGrade().doubleValue() > 5) {
            throw new InvalidGradeException("Передана оценка больше 5");
        }
        if (studentGrade.getStudentName().isEmpty()) {
            throw new InvalidStudentNameException("Имя студента не заполнено");
        }
        studentGradeList.add(studentGrade);
        System.out.println("Добавлена оценка. Студент: " + studentGrade.getStudentName() +
                ", предмет: " + studentGrade.getSubject() + ", оценка: " + studentGrade.getGrade());
    }

    public double getAvgGradeBySubject(Subjects subject) {
        return studentGradeList.stream().filter(data -> data.getSubject().equals(subject))
                .mapToDouble(data -> data.getGrade().doubleValue()).average().orElse(0);
    }
}
