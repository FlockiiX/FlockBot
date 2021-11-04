package de.flockiix.flockbot.core.scheduled;

import de.flockiix.flockbot.core.util.ArrayUtils;

import java.util.TimerTask;

public class ChangeMessages extends TimerTask {
    private static int errorMessage = 0;
    private static String errorMessageContent = "";

    public static String getErrorMessage() {
        return errorMessageContent;
    }

    @Override
    public void run() {
        if (errorMessage == ArrayUtils.ERROR_MESSAGES.size()) errorMessage = 0;
        errorMessageContent = ArrayUtils.ERROR_MESSAGES.get(errorMessage++);
    }
}
