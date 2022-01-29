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

/**
 * @param <E> the list type of the arguments
 * @param <T> the event
 */
public class CommandEvent<E, T> {
    private final List<E> args;
    private final Message message;
    private final User author;
    private final MessageChannel channel;
    private final Guild guild;
    private final T event;
    private final Bot bot;

    /**
     * Creates a CommandEvent
     *
     * @param args    the args
     * @param message the message of the event
     * @param author  the author of the event
     * @param channel the channel of the event
     * @param guild   the guild of the event
     * @param event   the event
     * @param bot     the bot instance
     */
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
            return ((GuildMessageReceivedEvent) event)
                    .getMember();
        }

        if (event instanceof SlashCommandEvent) {
            return ((SlashCommandEvent) event)
                    .getMember();
        }

        return null;
    }

    public User getUser() {
        return author;
    }

    // Actions

    /**
     * Sends a message into the channel of the event.
     *
     * @param text the text/message you want to send
     */
    public void reply(String text) {
        if (event instanceof SlashCommandEvent)
            ((SlashCommandEvent) event)
                    .reply(text)
                    .queue();
        else
            channel.sendMessage(text).queue();
    }

    /**
     * Sends an embed message into the channel of the event.
     *
     * @param embed the embed you want to send
     */
    public void reply(MessageEmbed embed) {
        if (event instanceof SlashCommandEvent)
            ((SlashCommandEvent) event)
                    .replyEmbeds(embed)
                    .queue();
        else
            channel.sendMessageEmbeds(embed).queue();
    }

    /**
     * Sends a message into the channel of the event.
     * If ephemeral is true only the command executor can read it.
     *
     * @param text      the text/message you want to send
     * @param ephemeral only command executor readability for the message
     */
    public void reply(String text, boolean ephemeral) {
        if (event instanceof SlashCommandEvent)
            ((SlashCommandEvent) event)
                    .reply(text)
                    .setEphemeral(ephemeral)
                    .queue();
        else
            channel.sendMessage(text).queue();
    }

    /**
     * Sends an embed into the channel of the event.
     * If ephemeral is true only the command executor can read it.
     *
     * @param embed     the embed you want to send
     * @param ephemeral only command executor readability for the embed
     */
    public void reply(MessageEmbed embed, boolean ephemeral) {
        if (event instanceof SlashCommandEvent)
            ((SlashCommandEvent) event)
                    .replyEmbeds(embed)
                    .setEphemeral(ephemeral)
                    .queue();
        else
            channel.sendMessageEmbeds(embed).queue();
    }

    /**
     * Sends a message into the channel of the event and deletes it after the given time.
     *
     * @param text    the text/message you want to send
     * @param seconds the number of seconds after which the message should be deleted
     */
    public void reply(String text, int seconds) {
        if (event instanceof SlashCommandEvent)
            ((SlashCommandEvent) event).reply(text).queue(
                    message -> message.deleteOriginal().queueAfter(
                            seconds, TimeUnit.SECONDS
                    )
            );
        else
            channel.sendMessage(text).queue(
                    message -> message.delete().queueAfter(
                            seconds, TimeUnit.SECONDS
                    )
            );
    }

    /**
     * Sends an embed into the channel of the event and deletes it after the given time.
     *
     * @param embed   the embed you want to send
     * @param seconds the number of seconds after which the message should be deleted
     */
    public void reply(MessageEmbed embed, int seconds) {
        if (event instanceof SlashCommandEvent)
            ((SlashCommandEvent) event).replyEmbeds(embed).queue(
                    message -> message.deleteOriginal().queueAfter(
                            seconds, TimeUnit.SECONDS
                    )
            );
        else
            channel.sendMessageEmbeds(embed).queue(
                    message -> message.delete().queueAfter(
                            seconds, TimeUnit.SECONDS
                    )
            );
    }

    public MessageAction replyAction(String text) {
        return channel.sendMessage(text);
    }

    public MessageAction replyAction(MessageEmbed embed) {
        return channel.sendMessageEmbeds(embed);
    }

    /**
     * Gets the usage of a command.
     *
     * @param command the command
     * @return the usage as a string
     */
    public String getUsage(Command command) {
        return "You used the command incorrectly.\n" +
                "Usage: `" + getPrefix() + command.getUsage() + "`";
    }

    /**
     * Gets the prefix of the guild.
     *
     * @return the custom prefix of the guild if set and otherwise the default prefix
     */
    public String getPrefix() {
        return SQLWorker.getPrefix(guild.getId());
    }

    /**
     * Creates an error embed with the message.
     *
     * @param text the text you want to set for the embed
     * @return the error embed with text
     */
    public MessageEmbed createErrorEmbed(String text) {
        return EmbedBuilderUtils
                .createErrorEmbed()
                .setDescription(text)
                .build();
    }

    /**
     * Creates a default embed with the message.
     *
     * @param text the text you want to set for the embed
     * @return the default embed with text
     */
    public MessageEmbed createDefaultEmbed(String text) {
        return EmbedBuilderUtils
                .createDefaultEmbed()
                .setDescription(text)
                .build();
    }

    /**
     * Gets a random error message.
     *
     * @param text the cause of the error
     * @return a random errormessage with the cause
     */
    public String getErrorMessage(String text) {
        return ChangeMessages.getErrorMessage() + " " + text;
    }
}
