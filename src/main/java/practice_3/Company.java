package practice_3;

public class Company {

    static String companyName;

    static {
        companyName = "Рога и копыта";
    }
    final int employeeID;
    String employeeName;
    Company(int employeeID, String employeeName){
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        System.out.println("Сотрудник работает в компании: " + companyName);
    }

    static void printCompanyName(){
        System.out.println("Имя компании: " + companyName);
    }

    public String getEmployeeName(){
        return this.employeeName;
    }

    public void setEmployeeName(String employeeName){
        this.employeeName = employeeName;
    }
}
