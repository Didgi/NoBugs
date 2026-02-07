package cleancode.complextasks.firsttask.strategies;

import cleancode.complextasks.firsttask.ShorteningStrategy;
import io.seruco.encoding.base62.Base62;

import java.nio.charset.StandardCharsets;

public class Base62Strategy implements ShorteningStrategy {
    private Base62 base62;

    public Base62Strategy() {
        this.base62 = Base62.createInstance();
    }

    @Override
    public String makeShortenUrl(String url) {
        final byte[] urlInBytes = url.getBytes(StandardCharsets.UTF_8);
        final byte[] encodedUrlInBytes = base62.encode(urlInBytes);
        return new String(encodedUrlInBytes);
    }
}
