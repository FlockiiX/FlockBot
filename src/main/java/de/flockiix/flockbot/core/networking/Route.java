package de.flockiix.flockbot.core.networking;

import java.lang.reflect.Type;

public record Route(RequestMethod requestMethod, String route, Type responseType) {

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getRoute() {
        return route;
    }

    public Type getResponseType() {
        return responseType;
    }
}
