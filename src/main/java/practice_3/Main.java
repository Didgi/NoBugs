package practice_3;

public class Main {

    public static void main(String[] args) {
        //COMPANY
    Company employee1 = new Company(1, "Сотрудник 1");
    Company employee2 = new Company(2, "Сотрудник 2");
    Company employee3 = new Company(3, "Сотрудник 3");
    Company.companyName = "Теперь не рога и копыта";
    Company employee4 = new Company(3, "Сотрудник 3");
    Company.printCompanyName();
//    employee1.employeeID = 2; //java: cannot assign a value to final variable employeeID


        //MATH_CONSTATNS
    MathConstants.calculateCircleArea(50);
    MathConstants.calculateCircumference(33);

        //UNIVERSITY
    University university1 = new University(1, "ПервыйСтудент");
    University university2 = new University(2, "ВторойСтудент");
    University university3 = new University(3, "ТретийСтудент");
    University.universityName = "РГРТУУУУУ";
    university1.printStudentInfo();
    university2.printStudentInfo();
    university3.printStudentInfo();


        //GAME_SETTINGS
    GameSettings cs = new GameSettings("Counter-Strike", 10);
    GameSettings dota = new GameSettings("Dota", 4);
    GameSettings.setMaxPlayers(5);
    cs.addPlayer();
    dota.addPlayer();
    cs.printGameStatus();
    dota.printGameStatus();

        //PERSON
    Person personIvan = new Person("Иван", "Иванов", "123-12-1234");
    personIvan.printPersonInfo();
    Person personSergey = new Person("Сергей", "Жуков", "777-77-7777");
    personSergey.printPersonInfo();
    Person personChanged = new Person("Персона", "Неизвестная", "000-00-0000");
    personChanged.printPersonInfo();
    personChanged.setFirstName("Валера");
    personChanged.setLastName("Леонтьев");
    personChanged.printPersonInfo();


    }

}
