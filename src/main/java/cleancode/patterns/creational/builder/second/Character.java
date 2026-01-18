package cleancode.patterns.creational.builder.second;

public class Character {

    private String name;
    private double health;
    private double power;
    private double armor;
    private double magic;

    public Character(String name, double health, double power, double armor, double magic) {
        this.name = name;
        this.health = health;
        this.power = power;
        this.armor = armor;
        this.magic = magic;
    }

    public Character(Builder builder){
        this.name = builder.name;
        this.health = builder.health;
        this.power = builder.power;
        this.armor = builder.armor;
        this.magic = builder.magic;
    }

    static class Builder{
        private String name;
        private double health;
        private double power;
        private double armor;
        private double magic;

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setHealth(double health) {
            this.health = health;
            return this;
        }

        public Builder setPower(double power) {
            this.power = power;
            return this;
        }

        public Builder setArmor(double armor) {
            this.armor = armor;
            return this;
        }

        public Builder setMagic(double magic) {
            this.magic = magic;
            return this;
        }

        public Character build(){
            return new Character(this);
        }
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", power=" + power +
                ", armor=" + armor +
                ", magic=" + magic +
                '}';
    }
}
