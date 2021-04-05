package metri.amit.sampleapp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by amitmetri on 04,April,2021
 * Helper class to obtain the retrofit object.
 * For debug builds logger is placed.
 * Retrofit object has RxJava as adapter factory.
 * Below improvements are pending to be implemented
 * ssl pinning, protection from ssl pinning bypassing (Ex: frida framework)
 */
public class ApiClient {

    public static Retrofit getClient(Context context) {

        OkHttpClient.Builder builder;
        builder = new OkHttpClient.Builder();

        /* For logging request and responses */
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        OkHttpClient okHttpClient = builder.build();

        Gson gson = new GsonBuilder()
                .setDateFormat(DateFormat.LONG)
                .create();

        /* Create retrofit object */
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url_retrofit))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }
}
