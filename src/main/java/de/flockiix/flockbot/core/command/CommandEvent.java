package de.flockiix.flockbot.core.command;

import de.flockiix.flockbot.core.scheduled.ChangeMessages;
import de.flockiix.flockbot.core.sql.SQLWorker;
import de.flockiix.flockbot.core.util.EmbedBuilderUtils;
import de.flockiix.flockbot.feature.Bot;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.List;

public class CommandEvent<E, T> {
    private final List<E> args;
    private final Message message;
    private final User author;
    private final MessageChannel channel;
    private final Guild guild;
    private final T event;
    private final Bot bot;

    protected CommandEvent(List<E> args, Message message, User author, MessageChannel channel, Guild guild, T event, Bot bot) {
        this.args = args;
        this.message = message;
        this.author = author;
        this.channel = channel;
        this.guild = guild;
        this.event = event;
        this.bot = bot;
    }

    public List<E> getArgs() {
        return args;
    }

    public Message getMessage() {
        return message;
    }

    public User getAuthor() {
        return author;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }

    public T getEvent() {
        return event;
    }

    public Bot getBot() {
        return bot;
    }

    public Member getSelfMember() {
        return guild.getSelfMember();
    }

    // Actions

    public void reply(String text) {
        channel.sendMessage(text).queue();
    }

    public void reply(MessageEmbed embed) {
        channel.sendMessageEmbeds(embed).queue();
    }

    public MessageAction replyAction(String text) {
        return channel.sendMessage(text);
    }

    public MessageAction replyAction(MessageEmbed embed) {
        return channel.sendMessageEmbeds(embed);
    }

    public String getUsage(Command command) {
        return "You used the command incorrectly.\n" +
                "Usage: `" + getPrefix() + command.getUsage() + "`";
    }

    public String getPrefix() {
        return SQLWorker.getPrefix(guild.getId());
    }

    public MessageEmbed createErrorEmbed(String text) {
        return EmbedBuilderUtils.createErrorEmbed().setDescription(text).build();
    }

    public String getErrorMessage() {
        return ChangeMessages.getErrorMessage();
    }
}
