package practice5.museum;

public class Sculpture extends Exhibit {

    public Sculpture(String history) {
        setHistory(history);
    }

    @Override
    public void getConditionStorage() {
        System.out.println("Скульптура нуждается в реставрации");
    }

    @Override
    public void setHistory(String history) {
        super.setHistory(history);
    }
}
