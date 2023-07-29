package prajna.app.repository.retrofitservice

import android.content.Context
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("Authorization", UserSession(context).getAccessToken().toString())
                .build()
        )
    }

}