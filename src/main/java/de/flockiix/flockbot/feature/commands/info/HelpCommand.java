package de.flockiix.flockbot.feature.commands.info;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.command.CommandHandler;
import de.flockiix.flockbot.core.util.EmbedBuilderUtils;
import de.flockiix.flockbot.core.util.Utils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HelpCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        String search = event.getArgs().size() == 1 ? event.getArgs().get(0) : null;
        execute(event, search);
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        String search = event.getArgs().size() == 1 ? event.getArgs().get(0).getAsString() : null;
        execute(event, search);
    }

    private void execute(CommandEvent<?, ?> event, String search) {
        var commandHandler = event.getBot().getCommandHandler();
        if (search == null) {
            event.reply(getCommandsEmbed(commandHandler));
            return;
        }

        var command = commandHandler.getCommand(search.toLowerCase());
        if (command == null || command.getCommandCategory() == CommandCategory.DEVELOPER && !Utils.isFlockiiX(event.getUser().getId())) {
            event.reply("This command does not exist");
            return;
        }

        MessageEmbed embed = EmbedBuilderUtils.createDefaultEmbed()
                .setTitle("Command help for " + event.getPrefix() + command.getName())
                .setDescription("```\n" +
                        "Description: " + command.getDescription() + "\n" +
                        "Usage: " + event.getPrefix() + command.getUsage() + "\n" +
                        "Aliases: " + command.getAliases() + "```\n" +
                        "**Legend: **`<> required, [] optional`\n" +
                        "**CoolDown: **`" + command.getCoolDown() + " seconds`\n" +
                        "**Required Member permission:**```\n" +
                        getRequiredPermissionsAsStringBuilder(command.getRequiredMemberPermissions()) + "```\n" +
                        "**Required Bot permission:**```\n" +
                        getRequiredPermissionsAsStringBuilder(command.getRequiredBotPermissions()) + "```")
                .build();
        event.reply(embed);
    }

    private MessageEmbed getCommandsEmbed(CommandHandler commandHandler) {
        EmbedBuilder embed = EmbedBuilderUtils.createDefaultEmbed()
                .setTitle("Commands");
        StringBuilder description = embed.getDescriptionBuilder();
        commandHandler.getCommands().stream().filter(command -> command.getCommandCategory() != CommandCategory.DEVELOPER).forEach(command -> description.append("`").append(command.getName()).append("`**/**"));
        return embed.build();
    }

    private StringBuilder getRequiredPermissionsAsStringBuilder(Set<Permission> permissionSet) {
        StringBuilder stringBuilder = new StringBuilder();
        new HashSet<>(permissionSet).forEach(permission -> stringBuilder.append("- ").append(permission.getName()).append("\n"));
        if (stringBuilder.isEmpty())
            stringBuilder.append("No permission required");

        return stringBuilder;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Sends help for the command";
    }

    @Override
    public String getUsage() {
        return "help [command]";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.STRING, "command", "The command you want to get help", false));
        return optionData;
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.INFO;
    }

    @Override
    public Set<String> getAliases() {
        return Set.of("elp", "command", "commands", "cmd", "cmds");
    }
}
