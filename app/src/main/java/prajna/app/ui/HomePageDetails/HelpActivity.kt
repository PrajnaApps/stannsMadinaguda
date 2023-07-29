package prajna.app.ui.HomePageDetails

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import prajna.app.R
import prajna.app.ui.Homescreen
import prajna.app.utills.customFonts.Text_Bold


class HelpActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var llWhatsapp: LinearLayout

    private var mTokenData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_layout)
//        mHallticket=UserSession(this@FeedbackActivity).getHallticket()

        mTokenData = UserSession(this@HelpActivity).getStudentKey()


        initialise()


    }

    private fun initialise() {

        llWhatsapp = findViewById(R.id.llWhatsapp)
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)

        ivBack.visibility=View.VISIBLE
        tvHeadertitle.text = "Help"



        llback.setOnClickListener(View.OnClickListener {
           onBackPressed()
        })
        llWhatsapp.setOnClickListener(View.OnClickListener {

            val number="8074901062"
            openWhatsappContact(number)
        })


    }
    fun openWhatsappContact(number: String) {
        val uri = Uri.parse("smsto:$number")
        val i = Intent(Intent.ACTION_SENDTO, uri)
        i.setPackage("com.whatsapp")
        startActivity(Intent.createChooser(i, ""))
    }
    fun openWhatsApp(view: View?) {
        try {
            val text = "This is a test" // Replace with your message.
            val toNumber =
                "xxxxxxxxxx" // Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //Create method appInstalledOrNot
    private fun appInstalledOrNot(url: String): Boolean {
        val packageManager = packageManager
        val app_installed: Boolean
        app_installed = try {
            packageManager.getPackageInfo(url, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }



    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Homescreen::class.java))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }
}
