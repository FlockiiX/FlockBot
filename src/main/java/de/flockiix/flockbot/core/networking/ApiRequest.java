package de.flockiix.flockbot.core.networking;

import com.google.gson.JsonSyntaxException;
import de.flockiix.flockbot.core.config.Config;
import de.flockiix.flockbot.core.exception.ApiException;
import de.flockiix.flockbot.core.exception.ApiRequestException;
import de.flockiix.flockbot.feature.Bot;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * Performs an api request.
 *
 * @param <T> the return type
 */
public class ApiRequest<T> {
    private static final String BASE_URL = Config.get("BASE_URL");
    private static final RequestBody EMPTY_BODY = RequestBody.create(null, new byte[0]);

    private final Bot bot;
    private final Route route;
    private final String url;
    private RequestBody body;

    public ApiRequest(Bot bot, Route route, String url, RequestBody body) {
        this.bot = bot;
        this.route = route;
        this.url = url;
        this.body = body;
    }

    public ApiRequest(Bot bot, Route route, RequestBody body) {
        this(bot, route, "", body);
    }

    public ApiRequest(Bot bot, Route route, String url) {
        this(bot, route, url, null);
    }

    public ApiRequest(Bot bot, Route route) {
        this(bot, route, "", null);
    }

    /**
     * Executes the api request.
     *
     * @param success the object
     * @param failure the throwable if an error occurs
     */
    public void executeRequest(Consumer<? super T> success, Consumer<? super Throwable> failure) {
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
                            failure.accept(new ApiException(response.body().string()));
                            return;
                        }

                        if (route.getResponseType() == Void.TYPE) {
                            success.accept(null);
                            return;
                        }

                        try {
                            // Converting json to object
                            assert response.body() != null;
                            String json = response.body().string();
                            success.accept(bot.getGson().fromJson(json, route.getResponseType()));
                        } catch (JsonSyntaxException | AssertionError exception) {
                            failure.accept(new ApiException(exception.getMessage()));
                        }
                    }
                });
    }

    public T executeRequest() {
        CompletableFuture<T> future = new CompletableFuture<>();
        executeRequest(future::complete, future::completeExceptionally);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException exception) {
            throw new ApiRequestException(exception.getMessage());
        }
    }

    private Request createRequest() {
        Request.Builder builder = new Request.Builder().url(BASE_URL + route.getUrl() + "/" + url).addHeader("Authorization", Config.get("API_AUTHORIZATION"));
        String method = route.getRequestMethod().toString();
        if (body == null && HttpMethod.requiresRequestBody(method))
            body = EMPTY_BODY;

        if (route.getRequestMethod() == RequestMethod.GET)
            return builder.get().build();

        return builder.method(method, body).build();
    }
}
