package practice5.amusement_park;

public class RollerCoaster extends Attraction {
    @Override
    public void getDescription() {
        System.out.println("Американские горки - быстрая езда");
    }

    @Override
    public void maintenance() {
        System.out.println("Американские горки требуют проверки безопасности");
    }
}
