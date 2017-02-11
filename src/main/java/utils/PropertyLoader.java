package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by vlad on 15.12.2016.
 */
public class PropertyLoader {
    public static Properties prop = new Properties();
    static {
        loadProperties();
    }

    public static void loadProperties() {
        InputStream in = PropertyLoader.class.getResourceAsStream("config/config.properties");
        try {
            prop.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
