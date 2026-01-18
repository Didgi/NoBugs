package cleancode.patterns.creational.builder.second;

public class Main {
    /*
    В игре игрок создает персонажа, задавая его параметры, такие как здоровье, урон, броня и магия.
    Паттерн Builder поможет организовать создание персонажа,
    позволяя задавать его параметры поэтапно и не перегружать конструктор класса Character.
     */

    public static void main(String[] args) {

    Character goblin = new Character.Builder()
            .setName("Подземный чёрный гоблин")
            .setHealth(10)
            .setArmor(0)
            .setPower(3)
            .build();
        System.out.println(goblin);
    }


}
