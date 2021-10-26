package de.flockiix.flockbot.feature.commands.info;

import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.config.Config;
import de.flockiix.flockbot.core.util.EmbedBuilderUtils;
import de.flockiix.flockbot.core.util.TimeUtils;
import de.flockiix.flockbot.feature.listeners.ButtonClickListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.Component;

import java.util.Collections;
import java.util.List;

public class BotCommand extends Command {
    private static final String INVITE_LINK = Config.get("INVITE_LINK");
    private static final Component INVITE_COMPONENT = Button.link(INVITE_LINK, "Invite me here");
    private static final Component NEWSLETTER_COMPONENT = Button.of(ButtonStyle.PRIMARY, ButtonClickListener.SUBSCRIBE_NEWSLETTER_ID, "Subscribe to FlockBot newsletter");

    @Override
    public void onCommand(CommandEvent<String, ?> event) {
        event.replyAction(getBotEmbed(System.currentTimeMillis()))
                .setActionRow(INVITE_COMPONENT, NEWSLETTER_COMPONENT)
                .queue();
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {
        event.getEvent().replyEmbeds(getBotEmbed(System.currentTimeMillis()))
                .addActionRow(INVITE_COMPONENT, NEWSLETTER_COMPONENT)
                .queue();
    }

    private MessageEmbed getBotEmbed(long startTime) {
        final JDA jda = BotInfo.jda;

        return EmbedBuilderUtils.createDefaultEmbed()
                .setTitle("FlockBot Information", INVITE_LINK)
                .setAuthor("FlockBot", "https://github.com/FlockiiX/FlockBot", jda.getSelfUser().getEffectiveAvatarUrl())
                .setDescription("Discord Bot built with [JDA](https://github.com/DV8FromTheWorld/JDA) Library")
                .addField("Bot", "```Version: [" + BotInfo.botVersion.version() + "]\n" +
                        "In Development: [" + BotInfo.botVersion.inDevelopment() + "]\n" +
                        "Uptime: [" + TimeUtils.getTime(BotInfo.startTime) + "]```", false)
                .addField("Client",
                        "```Servers: [" + jda.getGuilds().size() + "]\n" +
                                "Users: [" + jda.getUsers().size() + "]\n" +
                                "Ping: [" + jda.getGatewayPing() + "ms]\n" +
                                "ClientID: [" + jda.getSelfUser().getId() + "]\n" +
                                "Response Time: [" + (Integer.parseInt((System.currentTimeMillis() - startTime) + "")) + "ms" + "]```", false)
                .build();
    }

    @Override
    public String getName() {
        return "bot";
    }

    @Override
    public String getDescription() {
        return "Sends information about the bot";
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.INFO;
    }

    @Override
    public List<String> getAliases() {
        return List.of("stats", "information");
    }

    @Override
    public int getCoolDown() {
        return 10;
    }
}
