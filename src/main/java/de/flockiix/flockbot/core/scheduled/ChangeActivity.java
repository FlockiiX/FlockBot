package de.flockiix.flockbot.core.scheduled;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.util.ArrayUtils;
import net.dv8tion.jda.api.entities.Activity;

import java.util.TimerTask;

public class ChangeActivity extends TimerTask {
    private int i = 0;

    @Override
    public void run() {
        if (i == ArrayUtils.ACTIVITIES.size()) i = 0;
        BotInfo.jda.getPresence().setActivity(Activity.watching(ArrayUtils.ACTIVITIES.get(i++)));
    }
}
