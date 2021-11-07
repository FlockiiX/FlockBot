package de.flockiix.flockbot.feature.commands.moderator;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class KickCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.reply(event.getUsage(this));
            return;
        }

        Member target = event.getMessage().getMentionedMembers().get(0);
        String reason = event.getArgs().size() > 1 ? String.join(" ", event.getArgs().subList(1, event.getArgs().size())) : "";
        execute(event, target, reason);
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        Member target = event.getArgs().get(0).getAsMember();
        String reason = event.getArgs().size() > 1 ? event.getArgs().get(1).getAsString() : "";
        execute(event, target, reason);
    }

    private void execute(CommandEvent<?, ?> event, Member target, String reason) {
        if (!event.getSelfMember().canInteract(target) || target.hasPermission(Permission.KICK_MEMBERS)) {
            event.reply("I can't kick that user! The user has a higher role or is a moderator!");
            return;
        }

        if (!event.getMember().canInteract(target)) {
            event.reply("You can't kick that user!");
            return;
        }

        target.kick(reason).queue();
        event.reply("Successfully kicked " + target.getUser().getAsTag() + " from the server");
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Kicks a user from the guild";
    }

    @Override
    public String getUsage() {
        return "kick <@user> [reason]";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.USER, "user", "The user you want to kick", true));
        optionData.add(new OptionData(OptionType.STRING, "reason", "The reason for the kick", false));
        return optionData;
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.MODERATOR;
    }

    @Override
    public Set<Permission> getRequiredMemberPermissions() {
        return Set.of(Permission.KICK_MEMBERS);
    }

    @Override
    public Set<Permission> getRequiredBotPermissions() {
        return Set.of(Permission.KICK_MEMBERS);
    }
}
