package cleancode.patterns.structural.facade.first_2;

public class DoorFacade implements OpenDoor, CloseDoor, LockDoor {

    @Override
    public void close() {
        System.out.println("Закрытие двери");
    }

    @Override
    public void lock() {
        System.out.println("Блокировка двери");
    }

    @Override
    public void open() {
        System.out.println("Открытие двери");
    }
}
