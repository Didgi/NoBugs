package practice12_final.fifth_task;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AddProductToInventory extends BaseTest {
    /**
     * Позитивные:
     * Добавление товара в инвентарь при isInventoryOpen = true -> success
     * Добавление двух товаров разной категории в инвентарь при isInventoryOpen = true -> success
     * <p>
     * Негативные:
     * Добавление двух одинаковых товаров одной категории в инвентарь при isInventoryOpen = true -> success
     * Добавление товара в инвентарь при isInventoryOpen = false -> success
     */

    //Позитивный тест
    @Test
    @DisplayName("Успешное добавление товара в инвентарь при isInventoryOpen = true")
    public void addProductToInventoryIsSuccessWhenIsInventoryOpenTrue() {
        Product productAppleGreen = new Product("Яблоко зелёное", 70, "Фрукты");
        InventoryService.isInventoryOpen = true;
        int inventoryMapSizeInitial = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();
        assertEquals(0, inventoryMapSizeInitial);
        inventoryService.addProductToInventory(productAppleGreen);
        int inventoryMapSizeUpdated = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();

        assertEquals(1, inventoryMapSizeUpdated);

        checkDataInInventoryMap(productAppleGreen);
    }

    //Позитивный тест
    @Test
    @DisplayName("Успешное добавление двух товаров разной категории в инвентарь при isInventoryOpen = true")
    public void addProductInDiffCategoryToInventoryIsSuccessWhenIsInventoryOpenTrue() {
        Product productAppleGreen = new Product("Яблоко зелёное", 70, "Фрукты");
        Product productBeef = new Product("Говядина", 700, "Мясо");
        InventoryService.isInventoryOpen = true;
        int inventoryMapSizeInitial = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();
        assertEquals(0, inventoryMapSizeInitial);
        inventoryService.addProductToInventory(productAppleGreen);
        inventoryService.addProductToInventory(productBeef);
        int inventoryMapSizeUpdated = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();

        assertEquals(2, inventoryMapSizeUpdated);

        checkDataInInventoryMap(productAppleGreen);
        checkDataInInventoryMap(productBeef);

    }

    //Негативный тест
    @Test
    @DisplayName("Запрет добавления товара в инвентарь при isInventoryOpen = false")
    public void addProductToInventoryIsNotSuccessWhenIsInventoryOpenFalse() {
        Product productAppleGreen = new Product("Яблоко зелёное", 70, "Фрукты");
        InventoryService.isInventoryOpen = false;
        int inventoryMapSizeInitial = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();
        assertEquals(0, inventoryMapSizeInitial);
        inventoryService.addProductToInventory(productAppleGreen);
        int inventoryMapSizeUpdated = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();

        assertEquals(0, inventoryMapSizeUpdated);
    }

    //Негативный тест
    @Test
    @DisplayName("Успешное добавление двух одинаковых товаров одной категории в инвентарь при isInventoryOpen = true")
    public void addTwoEqualProductsSameCategoryToInventoryIsSuccessWhenIsInventoryOpenTrue() {
        Product productAppleGreen = new Product("Яблоко зелёное", 70, "Фрукты");
        InventoryService.isInventoryOpen = true;
        int inventoryMapSizeInitial = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();
        assertEquals(0, inventoryMapSizeInitial);
        inventoryService.addProductToInventory(productAppleGreen);
        inventoryService.addProductToInventory(productAppleGreen);
        int inventoryMapSizeUpdated = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();

        assertEquals(2, inventoryMapSizeUpdated);

        checkDataInInventoryMap(productAppleGreen);
    }

    public void checkDataInInventoryMap(Product product) {
        final Map.Entry<String, List<Product>> mapInventory = inventoryService.getInventoryMap()
                .entrySet().stream().filter(products ->
                        products.getKey().equals(product.getCategory())).findFirst().orElse(null);

        assertNotNull(mapInventory);
        mapInventory.getValue().forEach(value -> {
            assertEquals(value.getName(), product.getName());
            assertEquals(value.getPrice(), product.getPrice());
            assertEquals(value.getCategory(), product.getCategory());
        });
        assertEquals(mapInventory.getKey(), product.getCategory());
    }
}
