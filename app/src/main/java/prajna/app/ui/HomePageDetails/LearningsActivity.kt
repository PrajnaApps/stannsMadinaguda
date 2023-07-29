package prajna.app.ui.HomePageDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.ResponseBody
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.Homescreen
import prajna.app.utills.customFonts.Button_Normal
import prajna.app.utills.customFonts.EditeText_font
import prajna.app.utills.customFonts.Text_Bold
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class LearningsActivity : AppCompatActivity() {

    private var mFeedback: String = ""
    private var mHallticket: String = ""
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var btSubmitfeedback: Button_Normal
    private lateinit var etFeedback: EditeText_font

    private var mYearCode: String = ""
    private var mClassCode: String = ""
    private var mStudentKey: String = ""
    private var mTokenData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.events_layout)
//        mHallticket=UserSession(this@FeedbackActivity).getHallticket()

        mTokenData = UserSession(this@LearningsActivity).getStudentKey()

//        var dict_filterdata = JSONObject()
//        try {
//            dict_filterdata = JSONObject(mTokenData)
//            mStudentKey = dict_filterdata.getString("stud_key")
//            mYearCode = dict_filterdata.getString("stud_yearcode")
//            mClassCode = dict_filterdata.getString("stud_classcode")
////            mFatherName = dict_filterdata.getString("stud_father")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        webView.webChromeClient = object : WebChromeClient() {
//            private var mProgress: ProgressDialog? = null
//            override fun onProgressChanged(view: WebView, progress: Int) {
//                if (mProgress == null) {
//                    mProgress = ProgressDialog(activity)
//                    mProgress!!.show()
//                }
//                mProgress!!.setMessage("Loading...")
//                if (progress == 100) {
//                    mProgress!!.dismiss()
//                    mProgress = null
//                }
//            }
//        }
//

        initialise()


    }

    private fun initialise() {

        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)

        ivBack.visibility=View.VISIBLE
        tvHeadertitle.text = "Learnings"



        llback.setOnClickListener(View.OnClickListener {
           onBackPressed()
        })

    }
    private fun validate(): Boolean {
        var valid = true

        if (mFeedback.isEmpty()) {
//            etUsername.error = "Please enter Username"
            Toast.makeText(this@LearningsActivity, "Please enter Feedback", Toast.LENGTH_SHORT).show()
            valid = false
        }
        return valid
    }




    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Homescreen::class.java))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }
}
