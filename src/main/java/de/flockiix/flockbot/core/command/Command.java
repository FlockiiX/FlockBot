package de.flockiix.flockbot.core.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class Command {
    public abstract void onCommand(CommandEvent<String, ?> event);

    public abstract void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event);

    public abstract String getName();

    public abstract String getDescription();

    public abstract List<OptionData> getOptions();

    public abstract CommandCategory getCommandCategory();

    public String getUsage() {
        return this.getName();
    }

    public Set<Permission> getRequiredMemberPermissions() {
        return Set.of(Permission.EMPTY_PERMISSIONS);
    }

    public Set<Permission> getRequiredBotPermissions() {
        return Set.of(Permission.EMPTY_PERMISSIONS);
    }

    public Set<String> getAliases() {
        return Collections.emptySet();
    }

    public int getCoolDown() {
        return 5;
    }
}
