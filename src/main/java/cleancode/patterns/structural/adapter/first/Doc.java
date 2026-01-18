package cleancode.patterns.structural.adapter.first;

public class Doc implements Process {
    @Override
    public void process() {
        System.out.println("Загрузка и обработка Doc файла");
    }

    @Override
    public String toString() {
        return "DOC";
    }
}
