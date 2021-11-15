package de.flockiix.flockbot.feature.commands.developer;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import de.flockiix.flockbot.core.bot.BotInfo;
import de.flockiix.flockbot.core.command.Command;
import de.flockiix.flockbot.core.command.CommandCategory;
import de.flockiix.flockbot.core.command.CommandEvent;
import de.flockiix.flockbot.core.sql.SQLWorker;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NewsletterCommand extends Command {
    private final EventWaiter eventWaiter;

    public NewsletterCommand(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
    }

    @Override
    public void onCommand(CommandEvent<String, GuildMessageReceivedEvent> event) {
        var args = event.getArgs();
        if (args.isEmpty()) {
            event.getChannel().sendMessage("Enter valid arguments if you want to send a message").queue(message -> message.editMessage("Total newsletter subscribers: **" + SQLWorker.getNewsletterSubscribers().size() + "**").queueAfter(3, TimeUnit.SECONDS));
            return;
        }

        var message = String.join(" ", args);
        event.reply("Do you really want to send the following message to all newsletter subscribers?");
        event.getChannel().sendMessage("```" + message + "```").queue(
                channel -> eventWaiter.waitForEvent(
                        GuildMessageReceivedEvent.class,
                        e -> e.getChannel().equals(event.getChannel()) && e.getAuthor().equals(event.getAuthor()) && !e.getAuthor().isBot(),
                        e -> {
                            if (!e.getMessage().getContentRaw().equals("true")) {
                                event.reply("Message not sent");
                                return;
                            }

                            SQLWorker.getNewsletterSubscribers()
                                    .forEach(userId -> BotInfo.jda.retrieveUserById(userId).queue(
                                            user -> user.openPrivateChannel().submit().thenCompose(privateChannel -> privateChannel.sendMessage(message).submit())
                                    ));
                            event.reply("Newsletter sent successfully");
                        }, 15, TimeUnit.SECONDS, () -> event.reply("You took too long to answer")
                )
        );
    }

    @Override
    public void onSlashCommand(CommandEvent<OptionMapping, SlashCommandEvent> event) {

    }

    @Override
    public String getName() {
        return "newsletter";
    }

    @Override
    public String getDescription() {
        return "Sends a newsletter or shows you the total number of subscribers";
    }

    @Override
    public String getUsage() {
        return "newsletter [message]";
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }

    @Override
    public CommandCategory getCommandCategory() {
        return CommandCategory.DEVELOPER;
    }
}
