package prajna.app.repository.retrofitservice

import android.content.Context
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import prajna.app.utills.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceBuilder(val context: Context) {


    private var retrofitApi: Retrofit? = null
    private var retrofitMap:Retrofit? = null
    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    fun getRetrofit(): Retrofit? {
        if (retrofitApi == null) {
            val builder = OkHttpClient.Builder()
            val okHttpClient =
                builder.addInterceptor(AuthenticationInterceptor(context)).addNetworkInterceptor(
                    interceptor
                ).connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES).writeTimeout(5, TimeUnit.MINUTES)
                    .connectionPool(
                        ConnectionPool(0, 1, TimeUnit.SECONDS)
                    ).retryOnConnectionFailure(true).build()

            //http://68.178.172.246:80/

            retrofitApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        return retrofitApi
    }


}


