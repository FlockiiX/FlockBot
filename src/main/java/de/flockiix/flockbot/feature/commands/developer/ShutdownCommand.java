package de.flockiix.flockbot.feature.commands.developer;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.feature.Bot;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;

public class ShutdownCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {

    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {

    }

    @Override
    public void onPrivateMessageCommand(CommandEvent<String, PrivateMessageReceivedEvent> event) {
        event.reply("Shutting down");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Bot.sqlConnector.close();
        BotInfo.jda.shutdownNow();
        System.exit(0);
    }

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public String getDescription() {
        return "Shutdowns the bot";
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.DEVELOPER;
    }
}
