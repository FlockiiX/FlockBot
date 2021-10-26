package de.flockiix.flockbot.feature.listeners;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.scheduled.ChangeActivity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;

public class Listener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        BotInfo.jda = event.getJDA();
        new Timer().schedule(new ChangeActivity(), 0, 60 * 1000);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        final TextChannel channel = event.getGuild().getSystemChannel();
        if (channel == null)
            return;

        channel.sendMessage("Thank you for adding **FlockBot** to your guild!").queue();
    }
}
