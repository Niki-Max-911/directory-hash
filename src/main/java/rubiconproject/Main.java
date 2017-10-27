package rubiconproject;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Entry point.
 *
 * @author maksimnikitin
 * @since 25.10.17
 */
@Log
public class Main {

    static {
        loadProperties();
        setUpLogger();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            log.severe("one arguments expected: <path>");
            System.exit(1);
        }

        Path targetPath = Paths.get(args[0]);
        DirectoryHashService.performHashCalculating(targetPath);
    }

    private static void setUpLogger() {
        try (InputStream propertiesIS = Main.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(propertiesIS);
        } catch (IOException e) {
            log.severe("Log property loading fail.");
            log.throwing("Main", "setUpLogger", e);
        }
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
            log.throwing("Main", "loadProperties", e);
            System.exit(1);
        }
    }
}