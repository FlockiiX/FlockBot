package de.flockiix.flockbot.core.scheduled;

import de.flockiix.flockbot.core.util.ArrayUtils;

public class ChangeMessages {
    private static int errorMessage = 0;
    private static String errorMessageContent = "";

    public static String getErrorMessage() {
        return errorMessageContent;
    }

    public static Runnable changeMessages() {
        return () -> {
            if (errorMessage == ArrayUtils.ERROR_MESSAGES.size())
                errorMessage = 0;
            errorMessageContent = ArrayUtils.ERROR_MESSAGES.get(errorMessage++);
        };
    }
}
