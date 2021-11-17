package de.flockiix.flockbot.feature;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.bot.BotVersion;
import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandHandler;
import de.flockiix.flockbot.core.command.MessageListener;
import de.flockiix.flockbot.core.config.Config;
import de.flockiix.flockbot.core.sql.SQLConnector;
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
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
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
    private final EventWaiter eventWaiter;

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
        eventWaiter = new EventWaiter();

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
        }

        Reflections reflections = new Reflections("de.flockiix.flockbot.feature.commands");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
        List<Command> commands = new ArrayList<>();
        classes.forEach(aClass -> {
            try {
                Constructor<?> constructor = aClass.getConstructors()[0];
                Object object = constructor.newInstance();
                Command command = (Command) object;
                commands.add(command);
            } catch (Exception exception) {
                LOGGER.error("Failed to load commands", exception);
            }
        });

        commandHandler.registerCommands(commands.toArray(Command[]::new));
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

    public EventWaiter getEventWaiter() {
        return eventWaiter;
    }
}