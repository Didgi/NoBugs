package practice5.restaurant;


public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        Dish hotDish = new HotDish(71);
        Dish beverage = new Beverage(300);
        menu.addDish(hotDish);
        menu.addDish(beverage);
        menu.showDishesDescription();
    }
}
