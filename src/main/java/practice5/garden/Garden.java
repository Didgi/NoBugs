package practice5.garden;


public class Garden {
    private Plant plant;

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public void care() {
        this.plant.care();
    }
}
