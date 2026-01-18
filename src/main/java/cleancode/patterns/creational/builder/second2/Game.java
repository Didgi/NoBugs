package cleancode.patterns.creational.builder.second2;

public class Game {
    public Character createCharacter(String name, int health, int armor, int power, int magic) {
        return new Character.Builder()
                .setName(name)
                .setHealth(health)
                .setArmor(armor)
                .setPower(power)
                .setMagic(magic)
                .build();
    }


}
