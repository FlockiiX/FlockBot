package de.flockiix.flockbot.core.command;

import de.flockiix.flockbot.core.sql.SQLWorker;
import de.flockiix.flockbot.core.util.CommandUtils;
import de.flockiix.flockbot.core.util.EmbedBuilderUtils;
import de.flockiix.flockbot.core.util.PermissionUtils;
import de.flockiix.flockbot.core.util.Utils;
import de.flockiix.flockbot.feature.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;

@SuppressWarnings("Duplicates")
public class MessageListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListener.class);
    private final Bot bot;
    private final CommandHandler commandHandler;

    public MessageListener(Bot bot) {
        this.bot = bot;
        this.commandHandler = bot.getCommandHandler();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage())
            return;

        var message = event.getMessage();
        var args = message.getContentRaw().split("\\s");
        var guild = event.getGuild();
        var guildId = guild.getId();
        var channel = event.getChannel();
        var prefix = SQLWorker.getPrefix(guild.getId());
        var selfMember = guild.getSelfMember();
        var author = event.getAuthor();
        var userId = author.getId();

        if (SQLWorker.isBlacklistSet(guildId)) {
            if (isBadWordInMessage(guildId, message.getContentRaw()) && !event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                channel.sendMessage("You have used a blacklisted word. ").queue();
                return;
            }
        }

        if (!args[0].startsWith(prefix))
            return;

        var invoke = args[0].substring(prefix.length());
        var command = commandHandler.getCommand(invoke);

        if (command == null) {
            channel.sendMessage("This command does not exist").queue();
            return;
        }

        if (command.getCommandCategory() == CommandCategory.DEVELOPER && !Utils.isFlockiiX(userId)) {
            LOGGER.info("Non-Owner used Owner Command");
            return;
        }

        if (!selfMember.hasPermission(command.getRequiredBotPermissions())) {
            channel.sendMessageEmbeds(getMissingPermissionsEmbed(selfMember, command.getRequiredBotPermissions())).queue();
            return;
        }

        guild.retrieveMember(author).queue(member -> {
            if (!member.hasPermission(command.getRequiredMemberPermissions())) {
                channel.sendMessageEmbeds(getMissingPermissionsEmbed(member, command.getRequiredMemberPermissions())).queue();
                return;
            }

            if (CommandUtils.commandContainsCoolDown(command.getName(), userId) && !Utils.isFlockiiX(userId)) {
                long secondsLeft = ((CommandUtils.getCommandCoolDown(command.getName(), userId) / 1000) + command.getCoolDown()) - (System.currentTimeMillis() / 1000);
                if (secondsLeft > 0) {
                    message.reply("You cant use that commands for another " + secondsLeft + " seconds!").mentionRepliedUser(false).queue();
                    return;
                }
            }

            String[] split = message.getContentRaw().replaceFirst("(?i)" + Pattern.quote(SQLWorker.getPrefix(guild.getId())) + "|" + selfMember.getAsMention() + "( +)?", "").split("\\s+");
            CommandUtils.addCommandCoolDown(command.getName(), userId);
            CommandEvent<String, GuildMessageReceivedEvent> commandEvent = new CommandEvent<>(Arrays.asList(split).subList(1, split.length), message, author, channel, guild, event, bot);
            command.onCommand(commandEvent);
        });
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem())
            return;
        var message = event.getMessage();
        var args = message.getContentRaw().split("\\s+");
        var channel = event.getChannel();
        var author = event.getAuthor();
        var userId = author.getId();

        var invoke = args[0];
        var command = commandHandler.getCommand(invoke);

        if (command == null) {
            channel.sendMessage("This command does not exist").queue();
            return;
        }

        if (command.getCommandCategory() == CommandCategory.DEVELOPER && !Utils.isFlockiiX(userId)) {
            LOGGER.info("Non-Owner used Owner Command");
            return;
        }

        if (CommandUtils.commandContainsCoolDown(command.getName(), userId) && !Utils.isFlockiiX(userId)) {
            long secondsLeft = ((CommandUtils.getCommandCoolDown(command.getName(), userId) / 1000) + command.getCoolDown()) - (System.currentTimeMillis() / 1000);
            if (secondsLeft > 0) {
                message.reply("You cant use that commands for another " + secondsLeft + " seconds!").mentionRepliedUser(false).queue();
                return;
            }
        }

        CommandUtils.addCommandCoolDown(command.getName(), userId);
        CommandEvent<String, PrivateMessageReceivedEvent> commandEvent = new CommandEvent<>(Arrays.asList(args).subList(1, args.length), message, author, channel, null, event, bot);
        command.onPrivateMessageCommand(commandEvent);
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null)
            return;

        var invoke = event.getName();
        var command = commandHandler.getCommand(invoke);
        var guild = event.getGuild();
        var channel = event.getMessageChannel();
        var selfMember = guild.getSelfMember();
        var author = event.getUser();
        var userId = author.getId();

        if (command == null) {
            event.reply("This command does not exist").queue();
            return;
        }

        if (command.getCommandCategory() == CommandCategory.DEVELOPER && !Utils.isFlockiiX(userId)) {
            LOGGER.info("Non-Owner used Owner Command");
            return;
        }

        if (!selfMember.hasPermission(command.getRequiredBotPermissions())) {
            event.replyEmbeds(getMissingPermissionsEmbed(selfMember, command.getRequiredBotPermissions())).queue();
            return;
        }

        guild.retrieveMember(author).queue(member -> {
            if (!member.hasPermission(command.getRequiredMemberPermissions())) {
                event.replyEmbeds(getMissingPermissionsEmbed(member, command.getRequiredMemberPermissions())).queue();
                return;
            }

            if (CommandUtils.commandContainsCoolDown(command.getName(), userId) && !Utils.isFlockiiX(userId)) {
                long secondsLeft = ((CommandUtils.getCommandCoolDown(command.getName(), userId) / 1000) + command.getCoolDown()) - (System.currentTimeMillis() / 1000);
                if (secondsLeft > 0) {
                    event.reply("You cant use that commands for another " + secondsLeft + " seconds!").queue();
                    return;
                }
            }

            CommandUtils.addCommandCoolDown(command.getName(), userId);
            CommandEvent<OptionMapping, SlashCommandEvent> commandEvent = new CommandEvent<>(event.getOptions(), null, author, channel, guild, event, bot);
            command.onSlashCommand(commandEvent);
        });
    }

    private MessageEmbed getMissingPermissionsEmbed(Member member, Set<Permission> permissionSet) {
        EmbedBuilder permissionsEmbed = EmbedBuilderUtils.createErrorEmbed();
        String target = member.getUser().isBot() ? "The Bot is" : "You are";
        permissionsEmbed.setTitle(target + " missing following permissions:");

        StringBuilder stringBuilder = getMissingPermissions(member, permissionSet);

        permissionsEmbed.setDescription(stringBuilder.toString());

        return permissionsEmbed.build();
    }

    private StringBuilder getMissingPermissions(Member member, Set<Permission> permissionSet) {
        StringBuilder stringBuilder = new StringBuilder();
        PermissionUtils.getMissingPermissions(member, permissionSet).forEach(permission -> stringBuilder.append("- ").append(permission.getName()).append("\n"));

        return stringBuilder;
    }

    private boolean isBadWordInMessage(String guildId, String message) {
        ArrayList<String> words = SQLWorker.getBlackListWordsFromGuild(guildId);
        String[] args = message.toLowerCase().split(" ");

        for (String arg : args) {
            if (words.contains(arg.toLowerCase())) {
                return true;
            }
        }

        return false;
    }
}
