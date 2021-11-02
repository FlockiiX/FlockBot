package de.flockiix.flockbot.feature.listeners;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.model.Server;
import de.flockiix.flockbot.core.repository.impl.ServerRepositoryImpl;
import de.flockiix.flockbot.core.scheduled.ChangeActivity;
import de.flockiix.flockbot.core.scheduled.ChangeMessages;
import de.flockiix.flockbot.core.sql.SQLWorker;
import de.flockiix.flockbot.feature.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final Bot bot;

    public Listener(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        BotInfo.jda = event.getJDA();
        schedule();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        final TextChannel channel = event.getGuild().getSystemChannel();
        if (channel == null) return;

        var guildId = event.getGuild().getId();
        new ServerRepositoryImpl(bot).getServerByIdOrNull(guildId).executeRequest(
                found -> {
                    if (event.getGuild().getSelfMember().hasPermission(channel, Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE)) {
                        channel.sendMessage("I am back again :D\nThank you for adding **FlockBot** to your guild!").queue();
                    }
                }, notFound -> new ServerRepositoryImpl(bot).updateServer(new Server(guildId, SQLWorker.getPrefix(guildId), false)).executeRequest(
                        success -> {
                            if (event.getGuild().getSelfMember().hasPermission(channel, Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE)) {
                                channel.sendMessage("Thank you for adding **FlockBot** to your guild!").queue();
                            }
                        }, failure -> {
                            if (event.getGuild().getSelfMember().hasPermission(channel, Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE)) {
                                channel.sendMessage(ChangeMessages.getErrorMessage()).queue();
                            }
                        }
                )
        );
    }

    private void schedule() {
        Timer scheduler = new Timer();
        scheduler.schedule(new ChangeActivity(), 0, 60 * 1000);
        scheduler.schedule(new ChangeMessages(), 0, 15 * 1000);
    }
}
