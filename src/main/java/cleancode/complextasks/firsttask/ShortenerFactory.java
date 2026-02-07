package cleancode.complextasks.firsttask;

import cleancode.complextasks.firsttask.strategies.Base62Strategy;
import cleancode.complextasks.firsttask.strategies.MD5Strategy;

import java.security.NoSuchAlgorithmException;

public class ShortenerFactory {

    public static ShorteningStrategy chooseAlgorithmStrategy(EnumAlgorithms value) throws NoSuchAlgorithmException {
        switch (value) {
            case BASE_62:
                return new Base62Strategy();
            case MD5:
                return new MD5Strategy();
            default:
                return new Base62Strategy();
        }
    }
}
