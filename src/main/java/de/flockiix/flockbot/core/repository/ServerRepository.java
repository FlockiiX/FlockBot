package de.flockiix.flockbot.core.repository;

import de.flockiix.flockbot.core.model.Server;
import de.flockiix.flockbot.core.networking.ApiRequest;

public interface ServerRepository {
    ApiRequest<Server> getServerByIdOrNull(String serverId);

    ApiRequest<Server> updateServer(Server server);

    ApiRequest<Void> deleteServer(String serverId);
}
