package de.flockiix.flockbot.core.repository;

public record GuildRepository(String guildId, String prefix, boolean premiumGuild) {
    @Override
    public String guildId() {
        return guildId;
    }

    @Override
    public String prefix() {
        return prefix;
    }

    @Override
    public boolean premiumGuild() {
        return premiumGuild;
    }

    @Override
    public String toString() {
        return "GuildRequest{" +
                "guildId='" + guildId + '\'' +
                ", prefix='" + prefix + '\'' +
                ", premiumGuild=" + premiumGuild +
                '}';
    }
}
