package de.flockiix.flockbot.core.util;

public class TimeUtils {
    public static String getTime(long millisSeconds) {
        String end = "";

        long current = System.currentTimeMillis();
        long difference = current - millisSeconds;

        if (difference == 0) {
            return "0 Seconds";
        }

        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;

        while (difference >= 1000) {
            difference -= 1000;
            ++seconds;
        }
        while (seconds >= 60) {
            seconds -= 60;
            ++minutes;
        }
        while (minutes >= 60) {
            minutes -= 60;
            ++hours;
        }
        while (hours >= 24) {
            hours -= 24;
            ++days;
        }

        if (days != 0) {
            end += days + " Days" + (((hours != 0 || minutes != 0 || seconds != 0) ? ", " : ""));
        }

        if (hours != 0) {
            end += hours + " Hours" + (((minutes != 0 || seconds != 0) ? ", " : ""));
        }

        if (minutes != 0) {
            end += minutes + " Minutes" + ((seconds != 0 ? ", " : ""));
        }

        if (seconds != 0) {
            end += seconds + " Seconds";
        }

        return end;
    }
}
