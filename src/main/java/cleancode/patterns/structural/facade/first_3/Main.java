package cleancode.patterns.structural.facade.first_3;

public class Main {

    /*
    В нашей системе есть несколько операций для управления дверью:
Открытие двери.
Закрытие двери.
Блокировка двери.
Каждая операция реализована в своём классе. Задача — создать фасад,
который будет объединять эти операции и предоставлять простой интерфейс для работы с дверью.
     */

    public static void main(String[] args) {
        OpenDoor openDoor = new OpenDoor();
        CloseDoor closeDoor = new CloseDoor();
        LockDoor lockDoor = new LockDoor();


        DoorFacade doorFacade = new DoorFacade(openDoor, closeDoor, lockDoor);

        doorFacade.openDoor();
        doorFacade.closeDoor();
        doorFacade.lockDoor();
    }
}
