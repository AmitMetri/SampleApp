package metri.amit.sampleapp

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat

/**
 * Created by amitmetri on 04,April,2021
 * Helper class to obtain the retrofit object.
 * For debug builds logger is placed.
 * Retrofit object has RxJava as adapter factory.
 * Below improvements are pending to be implemented
 * ssl pinning, protection from ssl pinning bypassing (Ex: frida framework)
 */
object ApiClient {
    fun getClient(context: Context): Retrofit {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()

        /* For logging request and responses */
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BASIC
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        val okHttpClient = builder.build()
        val gson = GsonBuilder()
                .setDateFormat(DateFormat.LONG)
                .create()

        /* Create retrofit object */
        return Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url_retrofit))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }
}