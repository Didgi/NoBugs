package practice5_lesson;

public class Main {
    public static void main(String[] args) {
        Item mug = new Item("Кружка", 100, 5);
        Item microphone = new Electronics("Микрофон", 10000, 1);
        Item dress = new Clothes("Платье", 3000, 3);
        Manager manager = new Manager();
        manager.manage(mug);
        manager.manage(microphone);
        manager.manage(dress);
    }
}
