package ma.tp.retrofitjson.config;

import java.util.concurrent.TimeUnit;

import ma.tp.retrofitjson.api.ApiInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/"; // Common URL for both formats

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private static Retrofit retrofit;

    // Method to get Retrofit instance with flexibility for data format (JSON or XML)
    public static Retrofit getRetrofit(String format) {
        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client);

            // Select the converter based on the format
            if ("application/xml".equalsIgnoreCase(format)) {
                builder.addConverterFactory(SimpleXmlConverterFactory.create());
            } else {
                builder.addConverterFactory(GsonConverterFactory.create());
            }

            retrofit = builder.build();
        }
        return retrofit;
    }

    // Method to create and return the API interface instance based on format
    public static ApiInterface getApi(String format) {
        return getRetrofit(format).create(ApiInterface.class);
    }
}
