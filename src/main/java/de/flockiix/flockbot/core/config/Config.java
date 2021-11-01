package de.flockiix.flockbot.core.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.load();
    public static final int COLOR = Integer.parseInt(Config.get("color"), 16);

    /**
     * Searches for a configuration in the .env file.
     *
     * @param search the search key
     * @return the configuration, if present, otherwise null
     */
    public static String get(String search) {
        return dotenv.get(search.toUpperCase());
    }
}
