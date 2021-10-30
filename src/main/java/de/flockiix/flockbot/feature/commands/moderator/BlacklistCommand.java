package de.flockiix.flockbot.feature.commands.moderator;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.sql.SQLWorker;
import de.flockiix.flockbot.core.util.EmbedBuilderUtils;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class BlacklistCommand extends Command {
    @Override
    public void onCommand(CommandEvent<String, ?> event) {
        var args = event.getArgs();
        var guildId = event.getGuild().getId();
        if (args.isEmpty()) {
            var stringBuilder = getBlacklistWords(guildId);
            if (stringBuilder.length() > 4000) {
                event.reply("To many words");
                return;
            }

            var embed = EmbedBuilderUtils.createDefaultEmbed()
                    .setTitle("Blacklist")
                    .setDescription(stringBuilder.toString())
                    .build();

            event.reply(embed);
            return;
        }

        if (args.size() != 2) {
            event.reply(event.getUsage(this));
            return;
        }

        var action = args.get(0);
        var word = args.get(1);

        if (word.length() > 30) {
            event.reply("The word must not be longer than 30 characters");
            return;
        }

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
            default -> event.reply(event.getUsage(this));
        }
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        var args = event.getArgs();
        var action = args.get(0).getAsString();
        var word = args.get(1).getAsString();
        var guildId = event.getGuild().getId();

        if (word.length() > 30) {
            event.getEvent().reply("The word must not be longer than 30 characters").queue();
            return;
        }

        switch (action) {
            case "remove" -> {
                if (!SQLWorker.isWordOnBlackList(guildId, word)) {
                    event.getEvent().reply("**" + word + "** is not blacklisted").queue();
                    break;
                }
                SQLWorker.removeWordFromBlacklist(guildId, word);
                event.getEvent().reply("**" + word + "** was removed from the blacklist").queue();
            }
            case "add" -> {
                if (SQLWorker.isWordOnBlackList(guildId, word)) {
                    event.getEvent().reply("**" + word + "** is already blacklisted").queue();
                    break;
                }
                SQLWorker.addWordToBlacklist(guildId, word);
                event.getEvent().reply("**" + word + "** was added to the blacklist").queue();
            }
            default -> event.getEvent().reply(event.getUsage(this)).queue();
        }

        var stringBuilder = getBlacklistWords(guildId);
        if (stringBuilder.length() > 4000) {
            event.getChannel().sendMessage("To many words").queue();
            return;
        }

        var embed = EmbedBuilderUtils.createDefaultEmbed()
                .setTitle("Blacklist")
                .setDescription(stringBuilder.toString())
                .build();

        event.getChannel().sendMessageEmbeds(embed).queue();
    }

    private StringBuilder getBlacklistWords(String guildId) {
        StringBuilder stringBuilder = new StringBuilder();
        int blacklistSize = SQLWorker.getBlackListWordsFromGuild(guildId).size();
        for (int i = 0; i < blacklistSize; i++) {
            stringBuilder.append("**").append(SQLWorker.getBlackListWordsFromGuild(guildId).get(i)).append("**");
            if (i < blacklistSize - 1) {
                stringBuilder.append("|");
            }
        }

        if (stringBuilder.isEmpty()) {
            stringBuilder.append("There are currently no words on the blacklist. Use `").append(SQLWorker.getPrefix(guildId)).append("blacklist add <word>` to add some");
        }

        return stringBuilder;
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
        List<OptionData> optionData = new ArrayList<>();
        optionData.add(new OptionData(OptionType.STRING, "action", "The action you want to perform", true)
                .addChoice("add", "add")
                .addChoice("remove", "remove"));
        optionData.add(new OptionData(OptionType.STRING, "word", "The word you want to add/remove", true));
        return optionData;
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.MODERATOR;
    }
}
