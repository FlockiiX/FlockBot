package de.flockiix.flockbot.core.scheduled;

import de.flockiix.flockbot.core.util.ArrayUtils;

import java.util.TimerTask;

public class ChangeMessages extends TimerTask {
    private static int errorMessage = 0;

    public static String getErrorMessage() {
        return ArrayUtils.ERROR_MESSAGES.get(errorMessage);
    }

    @Override
    public void run() {
        if (errorMessage == ArrayUtils.ERROR_MESSAGES.size()) errorMessage = 0;
    }
}
