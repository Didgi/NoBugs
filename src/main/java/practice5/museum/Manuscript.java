package practice5.museum;

public class Manuscript extends Exhibit{


    public Manuscript(String history) {
        setHistory(history);
    }

    @Override
    public void getConditionStorage(){
        System.out.println("Манускрипт требует контролируемой влажности");
    }

    @Override
    public void setHistory(String history) {
        super.setHistory(history);
    }

    @Override
    public String getHistory(){
        return super.getHistory();
    }
}
