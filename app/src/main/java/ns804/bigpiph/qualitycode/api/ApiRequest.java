package ns804.bigpiph.qualitycode.api;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anatol on 9/10/2016.
 */
public class ApiRequest {

    static ApiRequest apiRequest;
    public static String token = "";

    public static final String BASE_URL = "http://iant.azurewebsites.net/api/";

    EngagementService service;

    public static ApiRequest getInstance() {
        if (apiRequest == null) {
            apiRequest = new ApiRequest();
        }
        return apiRequest;
    }

    public ApiRequest() {
        Retrofit retrofit = getRetrofit();
        service = retrofit.create(EngagementService.class);
    }

    public Retrofit getRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request ogRequest = chain.request();
                        Request.Builder requestBuilder = ogRequest.newBuilder();
//                        requestBuilder.addHeader("Authorization",
//                                "bearer aV7XGD8-ThBjYA3X5hJXn_BsXAom7iS2a4UU4eXHZwVo7kv7C1C4bmjqbi-pFzpE0j4yWtN3JjHoY-igztj6R0coflIGAzqk-QqOqmAgQL-hIwRiUzobKxaV_QAiJYkTxEUW5YTqITjHa_kKkhq38aSKdYJAqQfNLSnCXOMHApbKnBpx9iKP2G_Bh5rVAJ2eDqInhF9w04qPIBQRdFIMhf3CTF4s0gv0UCwAx7VQJunYCYZyJ59wztTqH8OXv7Y4X_TzAZEycrflQWa8lxZG1huWItOFyv0jOvD9o5xEKvqsQHkN_Pw8fZU3X1C_3ETAPqL39f2GYe6NGlpfIa31AdYmtxXUFyeNR7Mjh9V99sJNK7BJDpIJbrs8EPn0uelUMNAKdeJO7fmjAhCAfvuRXZ8AahZCcBcBVCaSnGu41HHskbXqs2bZUvFBQOxneGXjTuRfUwtNVLFU41CUUTiLoUbeMUCXxLvqH2ms04NydJiNE1dSSiKFhK1fglfVYFe9i4UNsljKkzWbgF11XDfKQD54iBIvi4riIA8Jl7bXOTY");
                        requestBuilder.addHeader("Authorization",
                                "bearer " + token);
                        Request request = requestBuilder.method(ogRequest.method(), ogRequest.body()).build();
                        Response response = chain.proceed(request);
                        Log.d("response", response.body().string());
                        return response;
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public EngagementService getService() {
        return service;
    }
}
