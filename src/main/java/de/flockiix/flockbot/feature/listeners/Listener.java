package de.flockiix.flockbot.feature.listeners;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.model.Server;
import de.flockiix.flockbot.core.repository.ServerRepository;
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

import java.util.concurrent.TimeUnit;

public class Listener extends ListenerAdapter {
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
        if (channel == null)
            return;

        var guild = event.getGuild();
        var guildId = guild.getId();

        if (ServerRepository.getServerByIdOrNull(bot, guildId) == null)
            return;

        if (guild.getSelfMember().hasPermission(channel, Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE)) {
            channel.sendMessage("I am back again :D\nThank you for adding **FlockBot** to your guild!").queue();
            return;
        }

        try {
            ServerRepository.updateServer(bot, new Server(guildId, SQLWorker.getPrefix(guildId), false));
            if (guild.getSelfMember().hasPermission(channel, Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE)) {
                channel.sendMessage("Thank you for adding **FlockBot** to your guild!").queue();
            }
        } catch (Exception exception) {
            if (guild.getSelfMember().hasPermission(channel, Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE)) {
                channel.sendMessage(ChangeMessages.getErrorMessage() + exception.getMessage()).queue();
            }
        }
    }

    private void schedule() {
        bot.getExecutorService().scheduleWithFixedDelay(ChangeMessages.changeMessages(), 0, 15, TimeUnit.SECONDS);
        bot.getExecutorService().scheduleWithFixedDelay(ChangeActivity.changeActivity(), 0, 60, TimeUnit.SECONDS);
    }
}
