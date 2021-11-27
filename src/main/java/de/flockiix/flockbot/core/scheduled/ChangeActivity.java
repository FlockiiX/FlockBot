package de.flockiix.flockbot.core.scheduled;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.util.ArrayUtils;
import net.dv8tion.jda.api.entities.Activity;

public class ChangeActivity {
    private static int i = 0;

    public static Runnable changeActivity() {
        return () -> {
            if (i == ArrayUtils.ACTIVITIES.size())
                i = 0;
            BotInfo.jda.getPresence().setActivity(Activity.watching(ArrayUtils.ACTIVITIES.get(i++)));
        };
    }
}
