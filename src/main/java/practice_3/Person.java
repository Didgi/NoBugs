package practice_3;

public class Person {

    private String firstName;
    private String lastName;
    private final String ssn;

    Person(String firstName, String lastName, String ssn){
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
    }

    String getFirstName(){
        return this.firstName;
    }

    String getLastName(){
        return this.lastName;
    }

    String getSsn(){
        return this.ssn;
    }

    void setFirstName(String newFirstName){
        this.firstName = newFirstName;
    }

    void setLastName(String newLastName){
        this.lastName = newLastName;
    }

    void printPersonInfo(){
        String result = String.format("Имя: %s, Фамилия: %s, SSN: %s", this.firstName, this.lastName, this.ssn);
        System.out.println(result);
    }
    /*
    Класс Person
Создайте класс Person с полями:
private String firstName
private String lastName
private final String ssn — номер социального страхования
Реализуйте конструктор для всех трёх полей, геттеры для всех полей, сеттеры только для firstName и lastName,
метод printPersonInfo() — выводит: "Имя: Иван, Фамилия: Иванов, SSN: 123-45-6789".
В main: создайте несколько объектов, измените имя у одного и выведите информацию.
     */
}
