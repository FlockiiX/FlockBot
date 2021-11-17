package de.flockiix.flockbot.feature.commands.util;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.util.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class RandomCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        var args = event.getArgs();
        if (args.size() != 2) {
            event.reply(event.getUsage(this));
            return;
        }

        execute(event, args.get(0), args.get(1));
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        var args = event.getArgs();
        execute(event, args.get(0).getAsString(), args.get(1).getAsString());
    }

    private void execute(CommandEvent<?, ?> event, String lowerBound, String upperBound) {
        if (lowerBound == null || upperBound == null) {
            event.reply(event.getUsage(this));
            return;
        }

        if (!Utils.isInteger(lowerBound) || !Utils.isInteger(upperBound)) {
            event.reply("Enter valid integers");
            return;
        }


        var lower = Integer.parseInt(lowerBound);
        var upper = Integer.parseInt(upperBound);

        if (upper <= lower) {
            event.reply("The upperbound must be greater than the lowerbound");
            return;
        }

        if (lower <= 0) {
            event.reply("Enter integers greater than 0");
            return;
        }

        var random = String.valueOf(Utils.random(lower, upper));
        event.reply(random);
    }

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public String getDescription() {
        return "Sends a random number between your bounds";
    }

    @Override
    public String getUsage() {
        return "random <lowerbound> <upperbound>";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.INTEGER, "lowerbound", "The lower bound", true));
        optionData.add(new OptionData(OptionType.INTEGER, "upperbound", "The upper bound", true));
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
