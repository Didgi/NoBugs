package practice6_collections.additionalTasks.fifth;

public class Main {
    public static void main(String[] args) {
        HistoryActionUser historyActionUser = new HistoryActionUser();
        historyActionUser.addAction("WroteAClass");
        historyActionUser.addAction("RewroteTheClass");
        System.out.println("Все действия пользователя " + historyActionUser.getHistoryUser());
        historyActionUser.undoAction();
        System.out.println("Все действия пользователя " + historyActionUser.getHistoryUser());
        historyActionUser.repeartLastAction();
        System.out.println("Все действия пользователя " + historyActionUser.getHistoryUser());
    }
}
