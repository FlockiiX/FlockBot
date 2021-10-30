package de.flockiix.flockbot.core.networking;

import com.google.gson.JsonSyntaxException;
import de.flockiix.flockbot.core.config.Config;
import de.flockiix.flockbot.core.exception.ApiException;
import de.flockiix.flockbot.feature.Bot;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

public class ApiRequest<T> {
    public static final String BASE_URL = Config.get("BASE_URL");
    public static final RequestBody EMPTY_BODY = RequestBody.create(null, new byte[0]);

    private final Bot bot;
    private final Route route;
    private RequestBody body;

    public ApiRequest(Bot bot, Route route, RequestBody body) {
        this.bot = bot;
        this.route = route;
        this.body = body;
    }

    public ApiRequest(Bot bot, Route route) {
        this(bot, route, null);
    }

    public void request(Consumer<? super T> success, Consumer<? super Throwable> failure) {
        bot.getHttpClient()
                .newCall(createRequest())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException exception) {
                        failure.accept(exception);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            failure.accept(new ApiException("Api request not successful"));
                            return;
                        }

                        if (route.getResponseType() == Void.TYPE) {
                            success.accept(null);
                            return;
                        }

                        try {
                            // Converting json to object
                            success.accept(bot.getGson().fromJson(Objects.requireNonNull(response.body()).string(), route.getResponseType()));
                        } catch (JsonSyntaxException exception) {
                            failure.accept(new ApiException("Failed to handle api request"));
                        }
                    }
                });
    }

    private Request createRequest() {
        Request.Builder builder = new Request.Builder().url(BASE_URL + route.getRoute());
        String method = route.getRequestMethod().toString();
        if (body == null && HttpMethod.requiresRequestBody(method))
            body = EMPTY_BODY;

        return builder.method(method, body).build();
    }
}
