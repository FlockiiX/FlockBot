package de.flockiix.flockbot.feature.commands.settings;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.sql.SQLWorker;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PrefixCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, ?> event) {
        if(event.getGuild() == null) {
            event.reply("You can only use this command on a guild");
            return;
        }

        var args = event.getArgs();
        if (args.isEmpty()) {
            event.reply(event.getUsage(this));
            return;
        }

        var prefix = String.join(" ", args);
        if (prefix.length() > 5) {
            event.reply("Maximum length of the prefix is 5 characters");
            return;
        }

        SQLWorker.setPrefix(event.getGuild().getId(), prefix);
        event.reply("Prefix set to `" + prefix + "`");
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        if(event.getGuild() == null) {
            event.getEvent().reply("You can only use this command on a guild").queue();
            return;
        }

        var prefix = event.getArgs().get(0).getAsString();
        if (prefix.length() > 5) {
            event.getEvent().reply("Maximum length of the prefix is 5 characters").queue();
            return;
        }

        SQLWorker.setPrefix(event.getGuild().getId(), prefix);
        event.getEvent().reply("Prefix set to `" + prefix + "`").queue();
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
