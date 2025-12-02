package practice12_final.third_task;

public class StudentGrade<T extends Number> {
    /**
     * Поля для имени студента, предмета и оценки.
     * Оценка должна быть типа T, который расширяет класс Number.
     * Конструктор для инициализации всех полей.
     * Геттеры для доступа к полям.
     */

    private String studentName;
    private Subjects subject;
    private T grade;

    public StudentGrade(String studentName, Subjects subject, T grade) {
        this.studentName = studentName;
        this.subject = subject;
        this.grade = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public Subjects getSubject() {
        return subject;
    }

    public T getGrade() {
        return grade;
    }
}
