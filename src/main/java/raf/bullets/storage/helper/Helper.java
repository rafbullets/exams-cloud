package raf.bullets.storage.helper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

public class Helper {
    public static String url(String path) {
        try {
            URI uri = new URI(EnvBag.getPropertyValue("raf.bullets.host-url")+"/"+path);
            return uri.normalize().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String base64Encode(String string) {
        return Base64.getUrlEncoder().encodeToString(string.getBytes());
    }

    public static String base64Decode(String string) {
        return new String(Base64.getUrlDecoder().decode(string));
    }
}
