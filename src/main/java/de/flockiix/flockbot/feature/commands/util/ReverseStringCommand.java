package de.flockiix.flockbot.feature.commands.util;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class ReverseStringCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        var args = event.getArgs();
        if (args.isEmpty()) {
            event.reply(event.getUsage(this));
            return;
        }

        String string = String.join(" ", args);
        execute(event, string);
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        execute(event, event.getArgs().get(0).getAsString());
    }

    private void execute(CommandEvent<?, ?> event, String string) {
        event.reply("Reversed: **" + new StringBuilder(string).reverse() + "**");
    }

    @Override
    public String getName() {
        return "reverse";
    }

    @Override
    public String getDescription() {
        return "Reverses your String";
    }

    @Override
    public String getUsage() {
        return "reverse <string>";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.STRING, "string", "The string you want to reverse", true));
        return optionData;
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.UTIL;
    }

    @Override
    public int getCoolDown() {
        return 10;
    }
}
