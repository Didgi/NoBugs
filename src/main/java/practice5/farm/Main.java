package practice5.farm;

public class Main {
    public static void main(String[] args) {
        Farm farm = new Farm();
        Cow cow = new Cow();
        Chicken chicken = new Chicken();
        farm.manage(cow);
        farm.manage(chicken);
    }
}
