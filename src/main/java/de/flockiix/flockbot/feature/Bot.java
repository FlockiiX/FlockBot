package de.flockiix.flockbot.feature;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.bot.BotVersion;
import de.flockiix.flockbot.core.command.CommandHandler;
import de.flockiix.flockbot.core.command.MessageListener;
import de.flockiix.flockbot.core.config.Config;
import de.flockiix.flockbot.core.sql.SQLConnector;
import de.flockiix.flockbot.feature.commands.developer.NewsletterCommand;
import de.flockiix.flockbot.feature.commands.developer.ShutdownCommand;
import de.flockiix.flockbot.feature.commands.info.BotCommand;
import de.flockiix.flockbot.feature.commands.info.InviteCommand;
import de.flockiix.flockbot.feature.commands.settings.PrefixCommand;
import de.flockiix.flockbot.feature.listeners.ButtonClickListener;
import de.flockiix.flockbot.feature.listeners.ReadyListener;
import net.dv8tion.jda.api.GatewayEncoding;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

/**
 * Base class to start the bot
 *
 * @author FlockiiX
 * @version 1.0
 * @since 1.0
 */
public class Bot {
    public static final SQLConnector sqlConnector = new SQLConnector();
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);
    private final CommandHandler commandHandler = new CommandHandler();

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

        EventWaiter eventWaiter = new EventWaiter();
        Object[] listeners = {
                new ReadyListener(),
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
                    new InviteCommand(),
                    new PrefixCommand(),
                    new BotCommand()
            );
        }
    }

    public static void main(String[] args) {
        new Bot(Config.get("token"));
    }

    /**
     * Returns the CommandHandler instance
     *
     * @return the CommandHandler instance
     * @since 1.0
     */
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }
}