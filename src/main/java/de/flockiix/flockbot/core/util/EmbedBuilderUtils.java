package de.flockiix.flockbot.core.util;

import de.flockiix.flockbot.core.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;

public class EmbedBuilderUtils {
    /**
     * Creates and returns an error embed.
     *
     * @return the EmbedBuilder
     */
    public static EmbedBuilder createErrorEmbed() {
        return new EmbedBuilder().setColor(Color.RED);
    }

    /**
     * Creates and returns a default embed.
     *
     * @return the EmbedBuilder
     */
    public static EmbedBuilder createDefaultEmbed() {
        return new EmbedBuilder()
                .setColor(Config.COLOR)
                .setFooter(Config.get("credit"))
                .setTimestamp(Instant.now());
    }
}
