package de.flockiix.flockbot.feature.buttons;

import de.flockiix.flockbot.core.button.Button;
import de.flockiix.flockbot.core.sql.SQLWorker;
import de.flockiix.flockbot.core.util.ButtonUtils;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

import static de.flockiix.flockbot.core.util.ButtonUtils.SUBSCRIBE_BUTTON;

public class UnsubscribeNewsletterButton extends Button {
    @Override
    public void onButtonEvent(ButtonClickEvent event) {
        var message = "Too bad you are leaving! If this was a mistake or you wish to subscribe again, just click the button below!";
        var user = event.getUser();
        var userId = user.getId();

        if (!SQLWorker.isNewsletterSubscribed(userId)) {
            message = "You have not subscribed to the newsletter";
        }

        SQLWorker.setNewsletter(userId, false);
        event.reply(message).addActionRow(SUBSCRIBE_BUTTON).queue();
    }

    @Override
    public String getButtonId() {
        return ButtonUtils.UNSUBSCRIBE_NEWSLETTER_ID;
    }
}
