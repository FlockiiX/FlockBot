package de.flockiix.flockbot.feature.listeners;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.networking.ApiRequest;
import de.flockiix.flockbot.core.networking.Routes;
import de.flockiix.flockbot.core.repository.GuildRepository;
import de.flockiix.flockbot.core.scheduled.ChangeActivity;
import de.flockiix.flockbot.core.util.Utils;
import de.flockiix.flockbot.feature.Bot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.RequestBody;
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
        new Timer().schedule(new ChangeActivity(), 0, 60 * 1000);
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        GuildRepository guildRepository = new GuildRepository(event.getGuild().getId(), "!", false);
        new ApiRequest<Void>(bot, Routes.REGISTER_GUILD, RequestBody.create(Utils.JSON, bot.getGson().toJson(guildRepository)))
                .request(s -> {
                }, throwable -> LOGGER.error(throwable.getMessage()));
        final TextChannel channel = event.getGuild().getSystemChannel();
        if (channel == null)
            return;
        if (event.getGuild().getSelfMember().hasPermission(channel, Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE)) {
            channel.sendMessage("Thank you for adding **FlockBot** to your guild!").queue();
        }
    }
}
