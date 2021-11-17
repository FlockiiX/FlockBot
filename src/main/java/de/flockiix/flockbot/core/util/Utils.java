package de.flockiix.flockbot.core.util;

import de.flockiix.flockbot.core.config.Config;
import okhttp3.MediaType;

import java.util.Objects;
import java.util.Random;

public class Utils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Random RANDOM = new Random();

    public static boolean isFlockiiX(String userId) {
        return Objects.equals(userId, Config.get("owner_id"));
    }

    public static boolean isInteger(String someString) {
        try {
            Integer.parseInt(someString);
        } catch (NumberFormatException | NullPointerException exception) {
            return false;
        }

        return true;
    }

    public static int random(int lowerbound, int upperbound) {
        return RANDOM.nextInt((upperbound - lowerbound) + 1) + lowerbound;
    }
}
