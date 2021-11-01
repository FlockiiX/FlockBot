package de.flockiix.flockbot.core.networking;

import de.flockiix.flockbot.core.model.Server;

public class Routes {
    public static final Route SEARCH_SERVER = new Route(RequestMethod.GET, "find_server", Server.class);
    public static final Route UPDATE_SERVER = new Route(RequestMethod.POST, "update_server", Server.class);
    public static final Route DELETE_SERVER = new Route(RequestMethod.DELETE, "delete_server", Void.class);
}
