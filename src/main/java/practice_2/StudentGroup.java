package practice_2;

public class StudentGroup {

    String groupName;
    int studentCount;

    StudentGroup(String groupName, int studentCount){
        this.groupName = groupName;
        this.studentCount = studentCount;
    }

    public String getGroupName(){
        return this.groupName;
    }

    public int getStudentCount(){
        return this.studentCount;
    }

    public void setGroupName(String newGroupName){
        this.groupName = newGroupName;
    }

    public void setStudentCount(int newCount){
        this.studentCount = newCount;
    }

    public void printInfo(){
        System.out.println("Группа " + this.groupName + " имеет численность " + this.studentCount + " человек");
    }
}
