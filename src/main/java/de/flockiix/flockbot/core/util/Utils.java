package de.flockiix.flockbot.core.util;

import de.flockiix.flockbot.core.config.Config;
import okhttp3.MediaType;

import java.util.Objects;

public class Utils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static boolean isFlockiiX(String userId) {
        return Objects.equals(userId, Config.get("owner_id"));
    }

    public static boolean isInteger(String someString) {
        try {
            Integer.parseInt(someString);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }

        return true;
    }
}
