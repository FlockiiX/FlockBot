package de.flockiix.flockbot.core.model;

public class Server {
    private String serverId;
    private String prefix;
    private Boolean premiumServer;

    public Server(String serverId, String prefix, Boolean premiumServer) {
        this.serverId = serverId;
        this.prefix = prefix;
        this.premiumServer = premiumServer;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean isPremiumServer() {
        return premiumServer;
    }

    public void setPremiumServer(boolean premiumServer) {
        this.premiumServer = premiumServer;
    }
}
