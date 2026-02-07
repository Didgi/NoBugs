package cleancode.complextasks.firsttask.strategies;

import cleancode.complextasks.firsttask.ShorteningStrategy;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Strategy implements ShorteningStrategy {

    private MessageDigest md5;

    public MD5Strategy() throws NoSuchAlgorithmException {
        this.md5 = MessageDigest.getInstance("MD5");
    }

    @Override
    public String makeShortenUrl(String url) {
        byte[] hashBytes = md5.digest(url.toLowerCase().getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
