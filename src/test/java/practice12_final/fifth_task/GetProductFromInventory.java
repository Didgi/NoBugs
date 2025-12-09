package practice12_final.fifth_task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GetProductFromInventory extends BaseTest {
    /**
     * Позитивные:
     * Получение не последнего товара в существующей категории -> success
     * Получение одного из нескольких одинаковых товаров одной категории -> success
     * <p>
     * Негативные:
     * Получение товаров несуществующей категории -> exception
     * Получение отсутствующего товара в существующей категории -> exception
     * Получение последнего товара в категории -> exception
     */

    //Позитивный тест
    @Test
    @DisplayName("Получение не последнего товара из существующей категории")
    public void getProductFromInventoryIsSuccessWhenProductInCategoryIsNotLast() throws OutOfStockException {
        Product productAppleGreen = new Product("Яблоко зелёное", 70, "Фрукты");
        Product productAppleRed = new Product("Яблоко красное", 90, "Фрукты");
        InventoryService.isInventoryOpen = true;
        inventoryService.addProductToInventory(productAppleGreen);
        inventoryService.addProductToInventory(productAppleRed);
        int inventoryMapSizeInitial = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();

        assertEquals(2, inventoryMapSizeInitial);

        final Product productFromInventory = inventoryService.getProductFromInventory(productAppleGreen);
        checkProductFromInventory(productFromInventory, productAppleGreen);
        int inventoryMapSizeUpdated = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();
        assertEquals(1, inventoryMapSizeUpdated);

    }

    //Позитивный тест
    @Test
    @DisplayName("Получение одного из нескольких одинаковых товаров одной категории")
    public void getOneOfEqualProductsFromSameCategoryFromInventoryIsSuccessWhenProductInCategoryIsNotLast() throws OutOfStockException {
        Product productAppleGreen = new Product("Яблоко зелёное", 70, "Фрукты");
        InventoryService.isInventoryOpen = true;
        inventoryService.addProductToInventory(productAppleGreen);
        inventoryService.addProductToInventory(productAppleGreen);
        int inventoryMapSizeInitial = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();

        assertEquals(2, inventoryMapSizeInitial);

        final Product productFromInventory = inventoryService.getProductFromInventory(productAppleGreen);
        checkProductFromInventory(productFromInventory, productAppleGreen);
        int inventoryMapSizeUpdated = inventoryService.getInventoryMap().values().stream().mapToInt(List::size).sum();
        assertEquals(1, inventoryMapSizeUpdated);
    }

    //Негативный тест
    @Test
    @DisplayName("Получение товаров из несуществующей категории")
    public void getProductFromInventoryThrowExceptionWhenCategoryDoesntExist() {
        Product productAppleGreen = new Product("Яблоко зелёное", 70, "Фрукты");
        assertThrows(OutOfStockException.class, () -> {
            inventoryService.getProductFromInventory(productAppleGreen);
        });
    }

    //Негативный тест
    @Test
    @DisplayName("Получение отсутствующего товара в существующей категории")
    public void getProductFromInventoryThrowExceptionWhenProductInCategoryDoesntExist() {
        Product productAppleGreen = new Product("Яблоко зелёное", 70, "Фрукты");
        Product productAppleRed = new Product("Яблоко красное", 90, "Фрукты");
        InventoryService.isInventoryOpen = true;
        inventoryService.addProductToInventory(productAppleGreen);

        assertThrows(OutOfStockException.class, () -> {
            inventoryService.getProductFromInventory(productAppleRed);
        });

    }

    //Негативный тест
    @Test
    @DisplayName("Получение последнего товара в существующей категории")
    public void getProductFromInventoryThrowExceptionWhenProductInCategoryIsLast() {
        Product productAppleGreen = new Product("Яблоко зелёное", 70, "Фрукты");
        InventoryService.isInventoryOpen = true;
        inventoryService.addProductToInventory(productAppleGreen);

        assertThrows(OutOfStockException.class, () -> {
            inventoryService.getProductFromInventory(productAppleGreen);
        });

    }

    public void checkProductFromInventory(Product productFromInventory, Product initialProduct) {
        assertEquals(productFromInventory.getName(), initialProduct.getName());
        assertEquals(productFromInventory.getPrice(), initialProduct.getPrice());
        assertEquals(productFromInventory.getCategory(), initialProduct.getCategory());
    }
}
