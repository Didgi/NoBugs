package practice5.museum;

public abstract class Exhibit {
    private String history;

    public String getHistory() {
        return this.history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public abstract void getConditionStorage();
}
