package practice5.farm;

public class Main {
    public static void main(String[] args) {
        Farm farm = new Farm();
        CareableFunctionable cow = new Cow();
        CareableFunctionable chicken = new Chicken();
        farm.manage(cow);
        farm.manage(chicken);
    }
}
