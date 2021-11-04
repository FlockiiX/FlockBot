package de.flockiix.flockbot.feature.commands.moderator;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.sql.SQLWorker;
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

public class WarnCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        var args = event.getArgs();
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.reply(event.getUsage(this));
            return;
        }

        var target = event.getMessage().getMentionedMembers().get(0);
        String action = args.size() != 2 ? null : args.get(1);
        execute(event, target, action);
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        var args = event.getArgs();
        var target = args.get(0).getAsMember();
        String action = args.size() != 2 ? null : args.get(1).getAsString();
        execute(event, target, action);
    }

    private void execute(CommandEvent<?, ?> event, Member target, String action) {
        if (target == null) {
            event.reply("Enter a valid user");
            return;
        }

        var userId = target.getId();
        var guildId = event.getGuild().getId();

        if (action == null) {
            event.reply("**" + target.getEffectiveName() + "** has `" + SQLWorker.getWarnings(guildId, userId) + "` Warnings!");
            return;
        }

        if (!event.getSelfMember().canInteract(target) || !event.getMember().canInteract(target) || target.getUser().isBot()) {
            event.reply("The user is also a moderator. Can't warn him");
            return;
        }

        switch (action) {
            case "remove" -> {
                if (SQLWorker.getWarnings(guildId, userId) == 0) {
                    event.reply("The user has no warnings");
                    break;
                }

                SQLWorker.setWarnings(guildId, userId, SQLWorker.getWarnings(guildId, userId) - 1);
                event.reply("Warning removed! " + SQLWorker.getWarnings(guildId, userId) + "/5 warnings to be kicked");
            }
            case "add" -> {
                SQLWorker.setWarnings(guildId, userId, SQLWorker.getWarnings(guildId, userId) + 1);
                event.reply("Warning added! " + SQLWorker.getWarnings(guildId, userId) + "/5 warnings to be banned");
                if (SQLWorker.getWarnings(guildId, userId) >= 5) {
                    SQLWorker.setWarnings(guildId, userId, 0);
                    event.getGuild().ban(target, 2, "Warnings").queue();
                    event.getChannel().sendMessage("User banned successfully").queue();
                }
            }
            default -> event.reply(event.getUsage(this));
        }
    }

    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "Warns a user";
    }

    @Override
    public String getUsage() {
        return "warn <@user> <add/remove>";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.USER, "user", "The user you want to warn", true));
        optionData.add(new OptionData(OptionType.STRING, "action", "The action you want to perform", false)
                .addChoice("add", "add")
                .addChoice("remove", "remove")
        );
        return optionData;
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.MODERATOR;
    }

    @Override
    public Set<Permission> getRequiredMemberPermissions() {
        return Set.of(Permission.MESSAGE_MANAGE);
    }

    @Override
    public Set<Permission> getRequiredBotPermissions() {
        return Set.of(Permission.BAN_MEMBERS);
    }
}
