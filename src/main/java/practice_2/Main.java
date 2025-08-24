package practice_2;

public class Main {
    public static void main(String[] args) {
        //CAR
        Car car1 = new Car("Audi", 2024);
        car1.print();
        car1.setYear(2025);
        car1.print();

        //RECTANGLE
        Rectangle rectangle1 = new Rectangle(5.5, 7.11);
        System.out.println("Площадь прямоугольника = " + rectangle1.calculateArea());
        rectangle1.setWidth(10000.0);
        System.out.println("Площадь прямоугольника после изменений = " + rectangle1.calculateArea());

        //BOOK
        Book book1 = new Book("Ведьмак", "Сарковский");
        book1.printInfo();
        book1.setAuthor("Сапковский");
        book1.printInfo();

        //BANKACCOUNT
        BankAccount bankAccount1 = new BankAccount("Я", 100.1);
        bankAccount1.printBalance();
        bankAccount1.deposit(100);
        bankAccount1.withdraw(50);
        bankAccount1.printBalance();

        //POINT
        Point point1 = new Point(1.11, 2.22);
        point1.print();
        point1.setX(5.01);
        point1.print();

        //STUDENTGROUP
        StudentGroup studentGroup1 = new StudentGroup("946", 20);
        studentGroup1.printInfo();
        studentGroup1.setStudentCount(18);
        studentGroup1.printInfo();

        //CIRCLE
        Circle circle1 = new Circle(5);
        System.out.println("Площадь круга до изменений радиуса " + circle1.calculateArea());
        System.out.println("Длина окружности до изменений радиуса " + circle1.calculateCircumference());
        circle1.setRadius(10.99);
        System.out.println("Площадь круга после изменений радиуса " + circle1.calculateArea());
        System.out.println("Длина окружности после изменений радиуса " + circle1.calculateCircumference());

        //TEACHER
        Teacher teacher1 = new Teacher("Мария Ивановна", "Биология");
        teacher1.printInfo();
        teacher1.setSubject("Тестирование ПО");
        teacher1.printInfo();

        //PRODUCT
        Product product1 = new Product("Яблоки", 500);
        product1.printInfo();
        product1.setPrice(700);
        product1.applyDiscount(30);
        product1.printInfo();
        product1.applyDiscount(-10);

        //LAPTOP
        Laptop laptop1 = new Laptop("Apple", 1500);
        laptop1.printInfo();
        laptop1.setPrice(1000);
        laptop1.printInfo();
    }

}
