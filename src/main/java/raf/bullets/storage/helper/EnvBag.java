package raf.bullets.storage.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvBag {

    private static Properties prop;

    static{
        InputStream is = null;
        try {
            prop = new Properties();
            Resource resource = new ClassPathResource("application.properties");
            InputStream resourceInputStream = resource.getInputStream();
            prop.load(resourceInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPropertyValue(String key){
        return prop.getProperty(key);
    }

}
