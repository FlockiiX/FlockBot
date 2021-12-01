package de.flockiix.flockbot.core.util;

import de.flockiix.flockbot.core.config.Config;
import okhttp3.MediaType;

import java.util.Objects;
import java.util.Random;

public class Utils {
    /**
     * JSON MediaType
     */
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Random RANDOM = new Random();

    /**
     * Checks if the userid is FlockiiX.
     *
     * @param userId the user id you want to check
     * @return true if the user id is FlockiiX id and false otherwise
     */
    public static boolean isFlockiiX(String userId) {
        return Objects.equals(userId, Config.get("owner_id"));
    }

    /**
     * Checks a string for an integer.
     *
     * @param someString The string you want to check if it is an integer
     * @return true if the string is an integer and false otherwise
     */
    public static boolean isInteger(String someString) {
        try {
            Integer.parseInt(someString);
        } catch (NumberFormatException | NullPointerException exception) {
            return false;
        }

        return true;
    }

    /**
     * Gets a random number between two bounds.
     *
     * @param lowerBound the lower bound for the random number
     * @param upperBound the upper bound for the random number
     * @return the random number
     */
    public static int random(int lowerBound, int upperBound) {
        return RANDOM.nextInt((upperBound - lowerBound) + 1) + lowerBound;
    }
}
