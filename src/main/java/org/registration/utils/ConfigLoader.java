package org.registration.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ConfigLoader {
    private static final String CONFIG_FILE_NAME = "config.properties";

    public static Properties loadConfiguration() {
        Properties properties = new Properties();

        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (input == null) {
                throw new RuntimeException("No se puede encontrar: " + CONFIG_FILE_NAME);
            }

            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la configuración desde " + CONFIG_FILE_NAME, e);
        }

        return properties;
    }
}
