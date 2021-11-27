package de.flockiix.flockbot.feature.commands.developer;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShutdownCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        event.getMessage().delete().queue();
        execute(event);
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {

    }

    @Override
    public void onPrivateMessageCommand(CommandEvent<String, PrivateMessageReceivedEvent> event) {
        execute(event);
    }

    private void execute(CommandEvent<?, ?> event) {
        event.replyAction("Shutting down").queue(
                message -> message.delete().queueAfter(2, TimeUnit.SECONDS)
        );
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        event.getBot().getExecutorService().shutdown();
        BotInfo.sqlConnector.close();
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
