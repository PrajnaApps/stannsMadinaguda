package prajna.app.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import org.json.JSONObject
import prajna.app.R
import prajna.app.login.LoginActivity

class SplashScreen : AppCompatActivity() {

     var mUsername:String=""
     var mTokenData:String=""
    private  var mStudentKey: String=""


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
            Log.d("Test", "Granted")
        } else {
            // TODO: Inform user that that your app will not show notifications.
            Log.d("Test", "Failed")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        askNotificationPermission()
        getFCMToken()
        mUsername=UserSession(this@SplashScreen).getLoginSave()
        mTokenData = UserSession(this@SplashScreen).getStudentKey()

        val mNotify= UserSession(this@SplashScreen).getNotification()
        Log.e("TAG", "notify___"+ mNotify )

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        // This is used to hide the status bar and make 
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


//  mToken=UserSession(this@SplashScreen).gettokenData()
//        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({

            if(mUsername.equals("")){
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
                finish()
            }else{
                if(mStudentKey.equals("")){
                    startActivity(Intent(this@SplashScreen, SelectChildActivity::class.java))
                    overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
                    finish()
                }else{
                    startActivity(Intent(this@SplashScreen, Homescreen::class.java))
                    overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
                    finish()
                }

            }


        }, 3000) // 3000 is the delayed time in milliseconds.
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("Test", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            UserSession(this@SplashScreen).setDeviceId(token)
            // Log and toast
            Log.d("Test", "Token is $token")
        })
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}