package de.flockiix.flockbot.core.networking;

import de.flockiix.flockbot.core.model.Server;

public class Routes {
    /**
     * Route for searching/finding a server
     */
    public static final Route SEARCH_SERVER = new Route(RequestMethod.GET, "find_server", Server.class);
    /**
     * Route for updating a server
     */
    public static final Route UPDATE_SERVER = new Route(RequestMethod.POST, "update_server", Server.class);
    /**
     * Route for deleting a server
     */
    public static final Route DELETE_SERVER = new Route(RequestMethod.DELETE, "delete_server", Void.class);
}
