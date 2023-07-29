package prajna.app.utills

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo

object NetworkUtils {
    @SuppressLint("MissingPermission")
    fun isNetworkConnectionAvailable(context: Context): Boolean {
        var isNetworkConnectionAvailable = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null) {
            isNetworkConnectionAvailable = activeNetworkInfo.state == NetworkInfo.State.CONNECTED
        }
        return isNetworkConnectionAvailable
    }

    fun isGpsEnabled(context: Context): Boolean {
        var isGpsEnabled = false
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGpsEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isGpsEnabled
    }
}