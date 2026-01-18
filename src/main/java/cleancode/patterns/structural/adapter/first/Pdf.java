package cleancode.patterns.structural.adapter.first;

public class Pdf implements Process {
    @Override
    public void process() {
        System.out.println("Загрузка и обработка PDF файла");
    }

    @Override
    public String toString() {
        return "PDF";
    }
}
