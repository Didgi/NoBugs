package cleancode.patterns.structural.adapter.second;

public class MilesToKmAdapter{
    private Miles miles;

    private final double MILES_IN_ONE_KM = 1.609;

    public MilesToKmAdapter(Miles miles) {
        this.miles = miles;
    }

    public double convertMilesToKm(){
        return miles.getMilesDistance() * MILES_IN_ONE_KM;
    }
}
