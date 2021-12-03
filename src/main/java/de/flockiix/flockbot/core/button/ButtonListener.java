package de.flockiix.flockbot.core.button;

import de.flockiix.flockbot.feature.Bot;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ButtonListener extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ButtonListener.class);
    private final ButtonHandler buttonHandler;

    public ButtonListener(Bot bot) {
        this.buttonHandler = bot.getButtonHandler();
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (event.getUser().isBot() || event.getUser().isSystem())
            return;

        var buttonId = event.getComponentId();
        var button = buttonHandler.getButton(buttonId);
        if (button == null) {
            LOGGER.error("Button with id not found");
            return;
        }

        button.onButtonEvent(event);
    }
}
