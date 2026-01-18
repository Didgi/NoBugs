package cleancode.patterns.structural.adapter.second;

public class Kilometers implements Distance{
    private double kmDistance;

    public Kilometers(double kmDistance) {
        this.kmDistance = kmDistance;
    }

    public double getKmDistance() {
        return kmDistance;
    }

    @Override
    public double distance() {
        System.out.println("Дистанция в километрах: " + kmDistance);
        return kmDistance;
    }
}
