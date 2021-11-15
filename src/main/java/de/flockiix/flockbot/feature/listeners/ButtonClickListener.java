package de.flockiix.flockbot.feature.listeners;

import de.flockiix.flockbot.core.sql.SQLWorker;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

public class ButtonClickListener extends ListenerAdapter {
    public static final String SUBSCRIBE_NEWSLETTER_ID = "flockbot-subscribe-newsletter";
    public static final String UNSUBSCRIBE_NEWSLETTER_ID = "flockbot-unsubscribe-newsletter";

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (event.getUser().isBot() || event.getUser().isSystem())
            return;

        // NEWSLETTER
        final Component SUBSCRIBE_BUTTON = Button.of(ButtonStyle.PRIMARY, SUBSCRIBE_NEWSLETTER_ID, "Subscribe to FlockBot newsletter");
        final Component UNSUBSCRIBE_BUTTON = Button.of(ButtonStyle.SECONDARY, UNSUBSCRIBE_NEWSLETTER_ID, "Unsubscribe");

        if (event.getComponentId().equals(SUBSCRIBE_NEWSLETTER_ID)) {
            var message = "You have successfully subscribed to the newsletter!";
            var user = event.getUser();
            var userId = user.getId();

            if (SQLWorker.isNewsletterSet(userId) && !SQLWorker.isNewsletterSubscribed(userId)) {
                message = "Welcome back! It's nice to see you here again.";
            }

            if (SQLWorker.isNewsletterSubscribed(userId)) {
                message = "You have already subscribed to the newsletter.";
            }

            SQLWorker.setNewsletter(userId, true);
            final String messageForUser = message;
            if (event.getGuild() != null) {
                user.openPrivateChannel().submit()
                        .thenCompose(privateChannel -> privateChannel.sendMessage(messageForUser).setActionRow(UNSUBSCRIBE_BUTTON).submit())
                        .whenComplete((msg, throwable) -> {
                            if (throwable != null)
                                event.reply("Open your dms").queue();
                            else
                                event.reply("Sent you a private message").queue();
                        });
            } else {
                event.reply(messageForUser).addActionRow(UNSUBSCRIBE_BUTTON).queue();
            }
        }

        if (event.getComponentId().equals(UNSUBSCRIBE_NEWSLETTER_ID)) {
            var message = "Too bad you are leaving! If this was a mistake or you wish to subscribe again, just click the button below!";
            var user = event.getUser();
            var userId = user.getId();

            if (!SQLWorker.isNewsletterSubscribed(userId)) {
                message = "You have not subscribed to the newsletter";
            }

            SQLWorker.setNewsletter(userId, false);
            event.reply(message).addActionRow(SUBSCRIBE_BUTTON).queue();
        }
    }
}
