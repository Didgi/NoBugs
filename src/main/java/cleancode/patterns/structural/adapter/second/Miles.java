package cleancode.patterns.structural.adapter.second;

public class Miles implements Distance{
    private double milesDistance;

    public Miles(double milesDistance) {
        this.milesDistance = milesDistance;
    }

    public double getMilesDistance() {
        return milesDistance;
    }

    @Override
    public double distance() {
        System.out.println("Дистанция в милях: " + milesDistance);
        return milesDistance;
    }
}
