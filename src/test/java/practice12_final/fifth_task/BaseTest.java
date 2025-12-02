package practice12_final.fifth_task;

import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected InventoryService inventoryService;

    @BeforeEach
    public void setUp(){
        inventoryService = new InventoryService();
    }

}
