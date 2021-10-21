package de.flockiix.flockbot.core.scheduled;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.config.Config;
import net.dv8tion.jda.api.entities.Activity;

import java.util.TimerTask;

public class ChangeActivity extends TimerTask {
    private int i = 0;

    @Override
    public void run() {
        if (i == Config.ACTIVITIES.size()) i = 0;
        BotInfo.jda.getPresence().setActivity(Activity.watching(Config.ACTIVITIES.get(i++)));
    }
}
