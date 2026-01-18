package cleancode.patterns.creational.builder.second2;

public class Main {
    /*
    В игре игрок создает персонажа, задавая его параметры, такие как здоровье, урон, броня и магия.
    Паттерн Builder поможет организовать создание персонажа,
    позволяя задавать его параметры поэтапно и не перегружать конструктор класса Character.
     */

    public static void main(String[] args) {
        Game game = new Game();
        System.out.println(game.createCharacter("Подземный чёрный гоблин", 10, 0, 3, 0));
    }


}
