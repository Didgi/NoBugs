package practice5.restaurant;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private List<Dish> dishList = new ArrayList<>();


    public void addDish(Dish dish) {
        dishList.add(dish);
    }
    public void showDishesDescription() {
        for (Dish d : dishList) {
            d.printDescription();
        }
    }
}
