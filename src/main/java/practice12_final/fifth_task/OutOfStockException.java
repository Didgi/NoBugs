package practice12_final.fifth_task;

import java.io.IOException;

public class OutOfStockException extends IOException {
    public OutOfStockException(String message){
        super(message);
    }
}
