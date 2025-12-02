package practice12_final.fifth_task;

import java.util.*;

public class InventoryService {
    public static boolean isInventoryOpen = true;
    private Map<String, List<Product>> inventoryMap = new HashMap<>();

    public void addProductToInventory(Product product) {
        if (isInventoryOpen) {
            inventoryMap.computeIfAbsent(product.getCategory(), productKey -> new ArrayList<>()).add(product);
        } else {
            System.out.println("На данный момент запрещено добавлять товары в инвентарь");
        }
    }

    public Product getProductFromInventory(Product product) throws OutOfStockException {
        List<Product> products = inventoryMap.get(product.getCategory());
        if (products != null) {
            final Optional<Product> foundProductOptional = inventoryMap.values().stream().flatMap(List::stream)
                    .filter(p -> p.getName().equals(product.getCategory()) &&
                            p.getPrice() == product.getPrice()).findFirst();
            if (foundProductOptional.isPresent()) {
                Product foundProduct = foundProductOptional.get();
                products.remove(foundProduct);
                inventoryMap.put(product.getCategory(), products);
                if (products.isEmpty()) {
                    throw new OutOfStockException("В категории " + product.getCategory() + " отсутствуют товары");
                }
                return foundProduct;
            } else {
                throw new OutOfStockException("В категории " + product.getCategory()
                        + " отсутствует товар " + product.getName());
            }
        } else {
            throw new OutOfStockException("В категории " + product.getCategory() + " отсутствуют товары");
        }
    }

    public Map<String, List<Product>> getInventoryMap() {
        return inventoryMap;
    }
}
