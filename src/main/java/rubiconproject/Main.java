package rubiconproject;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    static {
        log.addHandler(new ConsoleHandler());
        loadProperties();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            log.severe("one arguments expected: <path>");
            System.exit(1);
        }

        try {
            Path targetPath = Paths.get(args[0]);
            DirectoryHashService.performHashCalculating(targetPath);
        } catch (IOException e) {
            log.severe("Excepting during file tree walking.");
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}