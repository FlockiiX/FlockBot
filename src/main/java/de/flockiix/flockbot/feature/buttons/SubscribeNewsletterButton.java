package de.flockiix.flockbot.feature.buttons;

import de.flockiix.flockbot.core.button.Button;
import de.flockiix.flockbot.core.sql.SQLWorker;
import de.flockiix.flockbot.core.util.ButtonUtils;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import static de.flockiix.flockbot.core.util.ButtonUtils.UNSUBSCRIBE_BUTTON;

public class SubscribeNewsletterButton extends Button {
    @Override
    public void onButtonEvent(ButtonClickEvent event) {
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

    @Override
    public String getButtonId() {
        return ButtonUtils.SUBSCRIBE_NEWSLETTER_ID;
    }
}
