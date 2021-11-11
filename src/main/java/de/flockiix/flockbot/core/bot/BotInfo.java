package de.flockiix.flockbot.core.bot;

import de.flockiix.flockbot.core.sql.SQLConnector;
import net.dv8tion.jda.api.JDA;

public class BotInfo {
    public static BotVersion botVersion;
    public static JDA jda;
    public static long startTime;
    public static SQLConnector sqlConnector;
}
