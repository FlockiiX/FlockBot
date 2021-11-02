package de.flockiix.flockbot.core.repository.impl;

import de.flockiix.flockbot.core.model.Server;
import de.flockiix.flockbot.core.networking.ApiRequest;
import de.flockiix.flockbot.core.networking.Routes;
import de.flockiix.flockbot.core.repository.ServerRepository;
import de.flockiix.flockbot.core.util.Utils;
import de.flockiix.flockbot.feature.Bot;
import okhttp3.RequestBody;

public class ServerRepositoryImpl implements ServerRepository {
    private final Bot bot;

    public ServerRepositoryImpl(Bot bot) {
        this.bot = bot;
    }

    @Override
    public ApiRequest<Server> getServerByIdOrNull(String serverId) {
        return new ApiRequest<>(bot, Routes.SEARCH_SERVER, serverId);
    }

    @Override
    public ApiRequest<Server> updateServer(Server server) {
        return new ApiRequest<>(bot, Routes.UPDATE_SERVER, RequestBody.create(Utils.JSON, bot.getGson().toJson(server)));
    }

    @Override
    public ApiRequest<Void> deleteServer(String serverId) {
        return new ApiRequest<>(bot, Routes.DELETE_SERVER, serverId);
    }
}
