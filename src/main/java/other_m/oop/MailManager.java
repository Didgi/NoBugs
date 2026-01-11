package other_m.oop;

public class MailManager {

    private Shipment shipment;
    public MailManager(){
    }

    public void addShipment(Shipment shipment){
        this.shipment = shipment;
    }

    public double calculateDelivery(){
        return shipment.getDistance() * shipment.getBasePrice();
    }

    @Override
    public String toString() {
        return this.shipment.toString();
    }

    public void printInfo(){
        shipment.printInfo();
    }
}
