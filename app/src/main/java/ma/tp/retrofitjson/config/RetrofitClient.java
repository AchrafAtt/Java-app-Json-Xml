package ma.tp.retrofitjson.config;

import java.util.concurrent.TimeUnit;

import ma.tp.retrofitjson.api.ApiInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static final String BASE_URL="http://10.0.2.2:8080/";

    static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

            .build();

    static  Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) .addConverterFactory(GsonConverterFactory.create())
            .build();

    public  static  Retrofit getRetrofit() {
        return retrofit;
    }

    public static  ApiInterface getApi(){
        return retrofit.create(ApiInterface.class);

    }












}
