package de.flockiix.flockbot.core.util;

import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.Component;

public class ButtonUtils {
    public static final String SUBSCRIBE_NEWSLETTER_ID = "flockbot-subscribe-newsletter";
    public static final String UNSUBSCRIBE_NEWSLETTER_ID = "flockbot-unsubscribe-newsletter";

    public static final Component SUBSCRIBE_BUTTON = Button.of(ButtonStyle.PRIMARY, SUBSCRIBE_NEWSLETTER_ID, "Subscribe to FlockBot newsletter");
    public static final Component UNSUBSCRIBE_BUTTON = Button.of(ButtonStyle.SECONDARY, UNSUBSCRIBE_NEWSLETTER_ID, "Unsubscribe");
}
