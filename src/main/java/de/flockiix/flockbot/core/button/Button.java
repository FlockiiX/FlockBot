package de.flockiix.flockbot.core.button;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;

public abstract class Button {
    public abstract void onButtonEvent(ButtonClickEvent event);

    public abstract String getButtonId();
}
