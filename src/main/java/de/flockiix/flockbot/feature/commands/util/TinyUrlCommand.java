package de.flockiix.flockbot.feature.commands.util;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class TinyUrlCommand extends Command {

    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        var args = event.getArgs();
        if (args.isEmpty()) {
            event.reply(event.getUsage(this));
            return;
        }

        var url = args.get(0);
        execute(event, url);
    }

    @Override
    public void onPrivateMessageCommand(CommandEvent<String, PrivateMessageReceivedEvent> event) {
        var args = event.getArgs();
        if (args.isEmpty()) {
            event.reply(event.getUsage(this));
            return;
        }

        var url = args.get(0);
        execute(event, url);
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        var url = event.getArgs().get(0).getAsString();
        execute(event, url);
    }

    private void execute(CommandEvent<?, ?> event, String url) {
        if (isNotUrl(url)) {
            event.reply("Provide a valid URL");
            return;
        }

        String apiUrl = "https://tinyurl.com/api-create.php?url=";
        try {
            URL shortUrl = new URL(apiUrl + url);
            Scanner scanner = new Scanner(shortUrl.openStream());
            String shortenedUrl = scanner.nextLine();
            event.reply("Shortened url: " + shortenedUrl);
        } catch (IOException exception) {
            event.reply(event.getErrorMessage("Couldn't short url"));
        }
    }

    private boolean isNotUrl(String url) {
        try {
            new URL(url).toURI();
            return false;
        } catch (URISyntaxException | MalformedURLException exception) {
            return true;
        }
    }

    @Override
    public String getName() {
        return "tinyurl";
    }

    @Override
    public String getUsage() {
        return "tinyurl <url>";
    }

    @Override
    public String getDescription() {
        return "Shortens the provided URL";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.STRING, "url", "The url you want to short", true));
        return optionData;
    }

    @Override
    public Set<String> getAliases() {
        return Set.of("shorturl", "shortenurl", "url", "short");
    }

    @Override
    public int getCoolDown() {
        return 60;
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.UTIL;
    }
}
