package practice_3;

public class University {

    static String universityName = "РГРТУ";
    final int studentId;
    String studentName;

    University(int studentId, String studentName){
        this.studentId = studentId;
        this.studentName = studentName;
    }

    static void changeUniversityName(String newName){
        universityName = newName;
    }

    String getStudentName(){
        return this.studentName;
    }

    void printStudentInfo(){
        System.out.println("Студент " + this.studentName + " под номером " + this.studentId +
                " учится в университете: " + universityName);
    }
}
