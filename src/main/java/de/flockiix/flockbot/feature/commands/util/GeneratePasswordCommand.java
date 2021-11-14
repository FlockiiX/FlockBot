package de.flockiix.flockbot.feature.commands.util;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.util.Utils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class GeneratePasswordCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        var args = event.getArgs();
        if (args.isEmpty()) {
            event.reply(event.getUsage(this));
            return;
        }

        execute(event, args.get(0));
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        execute(event, event.getArgs().get(0).getAsString());
    }

    @Override
    public void onPrivateMessageCommand(CommandEvent<String, PrivateMessageReceivedEvent> event) {
        var args = event.getArgs();
        if (args.isEmpty()) {
            event.reply(event.getUsage(this));
            return;
        }

        execute(event, args.get(0));
    }

    private void execute(CommandEvent<?, ?> event, String lengthArgument) {
        if (!Utils.isInteger(lengthArgument)) {
            event.reply("Enter a valid length");
            return;
        }

        int length = Integer.parseInt(lengthArgument);
        if (length > 60) {
            event.reply("You don't need a password exceeding 60");
            return;
        }

        String password = IntStream.generate(new Random()::nextInt)
                .limit(length)
                .mapToObj(i -> (char) (Math.abs(i) % 93 + 33))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();

        if (event.getGuild() == null) {
            event.reply("Here is your password: ```" + password + "```");
            return;
        }

        event.getAuthor().openPrivateChannel().queue(
                privateChannel -> privateChannel.sendMessage("Here is your password: ```" + password + "```").queue()
        );
        event.reply("Sent you a private message with your password");
    }

    @Override
    public String getName() {
        return "genpass";
    }

    @Override
    public String getDescription() {
        return "Generates a random password";
    }

    @Override
    public String getUsage() {
        return "genpass <length>";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.INTEGER, "length", "The length of your password", true));
        return optionData;
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.UTIL;
    }

    @Override
    public Set<String> getAliases() {
        return Set.of("password", "generatepassword");
    }

    @Override
    public int getCoolDown() {
        return 20;
    }
}
