package de.flockiix.flockbot.core.bot;

public record BotVersion(String version, boolean inDevelopment) {

    @Override
    public String version() {
        return version;
    }

    @Override
    public boolean inDevelopment() {
        return inDevelopment;
    }

    @Override
    public String toString() {
        return "BotVersion{" +
                "version='" + version + '\'' +
                ", inDevelopment=" + inDevelopment +
                '}';
    }
}
