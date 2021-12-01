package de.flockiix.flockbot.core.repository;

import de.flockiix.flockbot.core.model.Server;
import de.flockiix.flockbot.core.networking.ApiRequest;
import de.flockiix.flockbot.core.networking.Routes;
import de.flockiix.flockbot.core.util.Utils;
import de.flockiix.flockbot.feature.Bot;
import okhttp3.RequestBody;

public class ServerRepository {
    public static Server getServerByIdOrNull(Bot bot, String serverId) {
        ApiRequest<Server> request = new ApiRequest<>(bot, Routes.SEARCH_SERVER, serverId);
        try {
            return request.executeRequest();
        } catch (Exception exception) {
            return null;
        }
    }

    public static Server updateServer(Bot bot, Server server) {
        ApiRequest<Server> request = new ApiRequest<>(bot, Routes.UPDATE_SERVER, RequestBody.create(Utils.JSON, bot.getGson().toJson(server)));
        return request.executeRequest();
    }

    public static void deleteServer(Bot bot, String serverId) {
        ApiRequest<Server> request = new ApiRequest<>(bot, Routes.DELETE_SERVER, serverId);
        request.executeRequest();
    }
}
