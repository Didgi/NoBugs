package cleancode.fixcode.fourth;

public class OrderOld {
    public void processOrder() {
        System.out.println("Обрабатываем заказ...");
    }
    public void sendEmailConfirmation() {
        System.out.println("Отправляем письмо клиенту...");
    }
    public void generateInvoice() {
        System.out.println("Генерируем счет...");
    }
}
//Задача: Разделите класс Order на отдельные классы, каждый из которых выполняет только одну задачу.

