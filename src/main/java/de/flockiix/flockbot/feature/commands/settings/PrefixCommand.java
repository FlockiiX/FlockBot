package de.flockiix.flockbot.feature.commands.settings;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.model.Server;
import de.flockiix.flockbot.core.repository.ServerRepository;
import de.flockiix.flockbot.core.sql.SQLWorker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrefixCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        var args = event.getArgs();
        if (args.isEmpty()) {
            event.reply(event.getUsage(this));
            return;
        }

        var prefix = String.join(" ", args);
        execute(event, prefix);
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        var prefix = event.getArgs().get(0).getAsString();
        execute(event, prefix);
    }

    private void execute(CommandEvent<?, ?> event, String prefix) {
        if (prefix.length() > 5) {
            event.reply("Maximum length of the prefix is 5 characters");
            return;
        }

        try {
            Server server = ServerRepository.updateServer(event.getBot(), new Server(event.getGuild().getId(), prefix, null));
            SQLWorker.setPrefix(event.getGuild().getId(), prefix);
            event.reply("Prefix set to `" + server.getPrefix() + "`");
        } catch (Exception exception) {
            event.reply(event.getErrorMessage(exception.getMessage()));
        }
    }

    @Override
    public String getName() {
        return "prefix";
    }

    @Override
    public String getDescription() {
        return "Sets the prefix of the guild";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.STRING, "prefix", "The Prefix you want to set", true));
        return optionData;
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.SETTINGS;
    }

    @Override
    public String getUsage() {
        return "prefix <prefix>";
    }

    @Override
    public Set<Permission> getRequiredMemberPermissions() {
        return Set.of(Permission.ADMINISTRATOR);
    }

    @Override
    public Set<String> getAliases() {
        return Set.of("setprefix");
    }

    @Override
    public int getCoolDown() {
        return 60;
    }
}
