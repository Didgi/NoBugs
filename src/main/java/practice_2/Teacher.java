package practice_2;

public class Teacher {

    String name;
    String subject;

    Teacher(String name, String subject){
        this.name = name;
        this.subject = subject;
    }

    public String getName(){
        return this.name;
    }

    public String getSubject(){
        return this.subject;
    }

    public void setName(String newTeacherName){
        this.name = newTeacherName;
    }

    public void setSubject(String newSubject){
        this.subject = newSubject;
    }

    public void printInfo(){
        System.out.println("Учитель " + this.name + " преподает предмет " + this.subject);
    }
}
