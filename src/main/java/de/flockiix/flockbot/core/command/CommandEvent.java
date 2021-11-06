package de.flockiix.flockbot.core.command;

import de.flockiix.flockbot.core.scheduled.ChangeMessages;
import de.flockiix.flockbot.core.sql.SQLWorker;
import de.flockiix.flockbot.core.util.EmbedBuilderUtils;
import de.flockiix.flockbot.feature.Bot;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    public User getSelfUser() {
        return getSelfMember().getUser();
    }

    public Member getMember() {
        if (event instanceof GuildMessageReceivedEvent) {
            return ((GuildMessageReceivedEvent) event).getMember();
        }

        if (event instanceof SlashCommandEvent) {
            return ((SlashCommandEvent) event).getMember();
        }

        return null;
    }

    public User getUser() {
        return author;
    }

    // Actions

    public void reply(String text) {
        if (event instanceof SlashCommandEvent)
            ((SlashCommandEvent) event).reply(text).queue();
        else
            channel.sendMessage(text).queue();
    }

    public void reply(MessageEmbed embed) {
        if (event instanceof SlashCommandEvent)
            ((SlashCommandEvent) event).replyEmbeds(embed).queue();
        else
            channel.sendMessageEmbeds(embed).queue();
    }

    public void reply(String text, int seconds) {
        if (event instanceof SlashCommandEvent)
            ((SlashCommandEvent) event).reply(text).queue(
                    message -> message.deleteOriginal().queueAfter(seconds, TimeUnit.SECONDS)
            );
        else
            channel.sendMessage(text).queue(
                    message -> message.delete().queueAfter(seconds, TimeUnit.SECONDS)
            );
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

    public MessageEmbed createDefaultEmbed(String text) {
        return EmbedBuilderUtils.createDefaultEmbed().setDescription(text).build();
    }

    public String getErrorMessage(String text) {
        return ChangeMessages.getErrorMessage() + " " + text;
    }
}
