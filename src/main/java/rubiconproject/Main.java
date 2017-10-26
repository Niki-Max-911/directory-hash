package rubiconproject;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;

/**
 * Entry point.
 *
 * @author maksimnikitin
 * @since 25.10.17
 */
@Log
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        loadProperties();
        log.addHandler(new ConsoleHandler());
    }

    private static void loadProperties() {
        try (InputStream propertiesIS = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties properties = new Properties();
            properties.load(propertiesIS);
            properties.forEach((name, value) -> {
                System.setProperty(name.toString(), value.toString());
            });
        } catch (IOException e) {
            log.severe("Property loading fail.");
            e.printStackTrace();
        }
    }
}