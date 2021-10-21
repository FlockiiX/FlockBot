package de.flockiix.flockbot.core.util;

import de.flockiix.flockbot.core.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.Instant;

public class EmbedBuilderUtils {
    public static EmbedBuilder createErrorEmbed() {
        return new EmbedBuilder().setColor(Color.RED);
    }

    public static EmbedBuilder createDefaultEmbed() {
        return new EmbedBuilder()
                .setColor(Config.COLOR)
                .setFooter(Config.get("credit"))
                .setTimestamp(Instant.now());
    }
}
