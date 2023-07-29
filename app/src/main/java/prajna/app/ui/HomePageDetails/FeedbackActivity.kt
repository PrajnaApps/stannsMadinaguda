package prajna.app.ui.HomePageDetails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.ResponseBody
import org.json.JSONObject
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

class FeedbackActivity : AppCompatActivity() {

    private var mFeedback: String = ""
    private var mHallticket: String = ""
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var btSubmitfeedback: Button_Normal
    private lateinit var etFeedback: EditeText_font

    private var mYearCode: String = ""
    private var mClassCode: String = ""
    private var mAdmissionNo: String = ""
    private var mStudentKey: String = ""
    private var mTokenData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback_layout)
//        mHallticket=UserSession(this@FeedbackActivity).getHallticket()

        mTokenData = UserSession(this@FeedbackActivity).getStudentKey()

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")
            mYearCode = dict_filterdata.getString("stud_yearcode")
            mClassCode = dict_filterdata.getString("stud_classcode")
            mAdmissionNo = dict_filterdata.getString("stud_admissionNo")
//            mFatherName = dict_filterdata.getString("stud_father")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        initialise()


    }

    private fun initialise() {

        btSubmitfeedback = findViewById(R.id.btSubmitfeedback)
        etFeedback = findViewById(R.id.etFeedback)
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)

        ivBack.visibility=View.VISIBLE
        tvHeadertitle.text = "Feedback"

        btSubmitfeedback.setOnClickListener(View.OnClickListener {
            mFeedback=etFeedback.text.toString().trim()
            if (validate()) {
                val dict_json = JSONObject()
                val jsonParser = JsonParser()
                dict_json.put("Description", mFeedback)
                dict_json.put("YearCode", mYearCode)
                dict_json.put("CreatedBy", 1)
                dict_json.put("AdmissionNumber", mAdmissionNo)
                val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
                Log.e("TAG", "exper_json: " + gsonObject)
                setfeedback(gsonObject)

            }

        })

        llback.setOnClickListener(View.OnClickListener {
           onBackPressed()
        })

    }
    private fun validate(): Boolean {
        var valid = true

        if (mFeedback.isEmpty()) {
//            etUsername.error = "Please enter Username"
            Toast.makeText(this@FeedbackActivity, "Please enter Feedback", Toast.LENGTH_SHORT).show()
            valid = false
        }
        return valid
    }


    private fun setfeedback(gsonObject: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@FeedbackActivity).getRetrofit()!!.create()
        retrofitService.saveFeedback(gsonObject).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    etFeedback.setText("")
                    Toast.makeText(this@FeedbackActivity, "Feedback submitted ", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(this@FeedbackActivity, "Invalid Credentials", Toast.LENGTH_SHORT)
                        .show()

                }



            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@FeedbackActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Homescreen::class.java))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }
}
