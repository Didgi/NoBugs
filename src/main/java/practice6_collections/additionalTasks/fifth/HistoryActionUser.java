package practice6_collections.additionalTasks.fifth;

import java.util.Stack;

public class HistoryActionUser {

    private Stack<String> historyUser;

    public HistoryActionUser() {
        historyUser = new Stack<>();
    }

    public Stack<String> getHistoryUser() {
        return historyUser;
    }

    public void addAction(String action) {
        System.out.println("Пользователь выполнил действие: " + historyUser.push(action));
    }

    public void undoAction() {
        System.out.println("Пользователь отменил последнее действие: " + historyUser.pop());
    }

    public void repeatLastAction() {
        System.out.println("Пользователь повторил последнее действие: " + historyUser.push(historyUser.peek()));
    }

}
