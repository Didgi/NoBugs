package other_m.oop;

public class Main {
    public static void main(String[] args) {
        Shipment letter = new Letter(20, 150);
        Shipment basicPackage = new BasicPackage(50, 20);
        Shipment fragilePackage = new FragilePackage(10, 200);
        MailManager mailManagerLetter = new MailManager();
        mailManagerLetter.addShipment(letter);
        mailManagerLetter.addShipment(basicPackage);
        mailManagerLetter.addShipment(fragilePackage);
//        System.out.println("Стоимость доставки письма: " + mailManagerLetter.calculateDelivery());
        mailManagerLetter.printInfo();
//        System.out.println(mailManagerLetter);
//        System.out.println("Стоимость доставки базовой посылки: " + mailManagerBasic.calculateDelivery());
//        System.out.println(mailManagerBasic);
//        System.out.println("Стоимость доставки хрупкой посылки: " + mailManagerFragile.calculateDelivery());
//        System.out.println(mailManagerFragile);


    }
}
