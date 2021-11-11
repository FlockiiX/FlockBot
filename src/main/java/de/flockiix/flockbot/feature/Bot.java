package de.flockiix.flockbot.feature;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.bot.BotVersion;
import de.flockiix.flockbot.core.command.CommandHandler;
import de.flockiix.flockbot.core.command.MessageListener;
import de.flockiix.flockbot.core.config.Config;
import de.flockiix.flockbot.core.sql.SQLConnector;
import de.flockiix.flockbot.feature.commands.developer.DebugCommand;
import de.flockiix.flockbot.feature.commands.developer.NewsletterCommand;
import de.flockiix.flockbot.feature.commands.developer.ShutdownCommand;
import de.flockiix.flockbot.feature.commands.info.BotCommand;
import de.flockiix.flockbot.feature.commands.info.HelpCommand;
import de.flockiix.flockbot.feature.commands.info.InviteCommand;
import de.flockiix.flockbot.feature.commands.moderator.BanCommand;
import de.flockiix.flockbot.feature.commands.moderator.BlacklistCommand;
import de.flockiix.flockbot.feature.commands.moderator.KickCommand;
import de.flockiix.flockbot.feature.commands.moderator.WarnCommand;
import de.flockiix.flockbot.feature.commands.settings.PrefixCommand;
import de.flockiix.flockbot.feature.listeners.ButtonClickListener;
import de.flockiix.flockbot.feature.listeners.Listener;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

/**
 * Base class to start the bot
 *
 * @author FlockiiX
 * @version 1.0
 * @since 1.0
 */
public class Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    private final CommandHandler commandHandler;
    private final OkHttpClient httpClient;
    private final Gson gson;

    /**
     * Starts and configures the bot
     *
     * @param token bot token
     * @since 1.0
     */
    private Bot(String token) {
        LOGGER.info("Booting...");
        BotInfo.startTime = System.currentTimeMillis();
        BotInfo.botVersion = new BotVersion(Config.get("version"), true);
        BotInfo.sqlConnector = new SQLConnector();

        commandHandler = new CommandHandler();
        httpClient = new OkHttpClient.Builder()
                .connectionPool(
                        new ConnectionPool(5, 10, TimeUnit.SECONDS)
                )
                .build();
        gson = new Gson();

        EventWaiter eventWaiter = new EventWaiter();
        Object[] listeners = {
                new Listener(this),
                new MessageListener(this),
                new ButtonClickListener(),
                eventWaiter
        };

        try {
            JDABuilder.createDefault(token)
                    .setMemberCachePolicy(MemberCachePolicy.VOICE)
                    .enableCache(EnumSet.of(
                            CacheFlag.VOICE_STATE,
                            CacheFlag.EMOTE
                    ))
                    .enableIntents(
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.GUILD_EMOJIS,
                            GatewayIntent.GUILD_MESSAGE_REACTIONS
                    )
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setActivity(Activity.watching("Online..."))
                    .addEventListeners(listeners)
                    .setBulkDeleteSplittingEnabled(false)
                    .setGatewayEncoding(GatewayEncoding.ETF)
                    .build().awaitReady();
            LOGGER.info("Bot started successfully");
        } catch (final LoginException | InterruptedException exception) {
            LOGGER.error("Failed to start Bot", exception);
        } finally {
            commandHandler.registerCommands(
                    new ShutdownCommand(),
                    new NewsletterCommand(eventWaiter),
                    new DebugCommand(),
                    new InviteCommand(),
                    new PrefixCommand(),
                    new BotCommand(),
                    new BlacklistCommand(),
                    new WarnCommand(),
                    new KickCommand(),
                    new BanCommand(),
                    new HelpCommand()
            );
        }
    }

    public static void main(String[] args) {
        new Bot(Config.get("token"));
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public Gson getGson() {
        return gson;
    }
}