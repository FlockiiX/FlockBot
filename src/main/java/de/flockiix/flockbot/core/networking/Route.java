package de.flockiix.flockbot.core.networking;

import java.lang.reflect.Type;

public record Route(RequestMethod requestMethod, String url, Type responseType) {

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public Type getResponseType() {
        return responseType;
    }
}
