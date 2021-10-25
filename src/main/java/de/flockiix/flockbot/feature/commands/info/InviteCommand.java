package de.flockiix.flockbot.feature.commands.info;

import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.config.Config;
import de.flockiix.flockbot.core.util.EmbedBuilderUtils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.Collections;
import java.util.List;

public class InviteCommand extends Command {
    private static final String INVITE_LINK = Config.get("INVITE_LINK");
    private static final MessageEmbed INVITE_EMBED = EmbedBuilderUtils.createDefaultEmbed()
            .setTitle("Invite me!")
            .setDescription("To invite the bot to one of your servers, you just have to press the button below this message")
            .build();

    @Override
    public void onCommand(CommandEvent<String, ?> event) {
        event.replyAction(INVITE_EMBED)
                .setActionRow(Button.link(INVITE_LINK, "Click here to invite me"))
                .queue();
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        event.getEvent().replyEmbeds(INVITE_EMBED)
                .addActionRow(Button.link(INVITE_LINK, "Click here to invite me"))
                .queue();
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getDescription() {
        return "Sends the invite link of the bot";
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.INFO;
    }
}
