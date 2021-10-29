package de.flockiix.flockbot.feature.commands.moderator;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.sql.SQLWorker;
import de.flockiix.flockbot.core.util.EmbedBuilderUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;

public class BlacklistCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, ?> event) {
        var args = event.getArgs();
        if (args.isEmpty()) {
            event.reply(getBlacklistWords(event.getGuild().getId()));
            return;
        }

        if (args.size() != 2) {
            event.reply(getUsage());
            return;
        }

        var action = args.get(0);
        var word = args.get(1);
        var guildId = event.getGuild().getId();
        switch (action) {
            case "remove" -> {
                if (!SQLWorker.isWordOnBlackList(guildId, word)) {
                    event.reply("**" + word + "** is not blacklisted");
                    break;
                }
                SQLWorker.removeWordFromBlacklist(guildId, word);
                event.reply("**" + word + "** was removed from the blacklist");
            }
            case "add" -> {
                if (SQLWorker.isWordOnBlackList(guildId, word)) {
                    event.reply("**" + word + "** is already blacklisted");
                    break;
                }
                SQLWorker.addWordToBlacklist(guildId, word);
                event.reply("**" + word + "** was added to the blacklist");
            }
            default -> event.reply(getUsage());
        }
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        // TODO: do the slash command
    }

    private MessageEmbed getBlacklistWords(String guildId) {
        StringBuilder stringBuilder = new StringBuilder();
        int blacklistSize = SQLWorker.getBlackListWordsFromGuild(guildId).size();
        for (int i = 0; i < blacklistSize; i++) {
            stringBuilder.append(SQLWorker.getBlackListWordsFromGuild(guildId).get(i));
            if (i < blacklistSize - 1) {
                stringBuilder.append("|");
            }
        }

        return EmbedBuilderUtils.createDefaultEmbed()
                .setTitle("Blacklist")
                .setDescription(stringBuilder.toString())
                .build();
    }

    @Override
    public String getName() {
        return "blacklist";
    }

    @Override
    public String getDescription() {
        return "Sets a word on the blacklist for the guild";
    }

    @Override
    public String getUsage() {
        return "blacklist <remove/add> <word>";
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.MODERATOR;
    }
}
