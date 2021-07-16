package util.http;

import org.asynchttpclient.*;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HttpHelper {
    private final DefaultAsyncHttpClientConfig.Builder clientBuilder;

    public HttpHelper() {
        this.clientBuilder = Dsl.config()
                .setConnectTimeout(500);
    }

    public JSONObject getResponseJson(String url) throws ExecutionException, InterruptedException {

        AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
        BoundRequestBuilder request = client.prepareGet(url);
        Future<Response> resp = request.execute();
        if (resp.get().getStatusCode() == 200) {
            return new JSONObject(resp.get().getResponseBody());
        }
        return null;
    }

    public DefaultAsyncHttpClientConfig.Builder getClientBuilder() {
        return clientBuilder;
    }
}
