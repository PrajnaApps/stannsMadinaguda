package prajna.app.ui.HomePageDetails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.EventsInfo
import prajna.app.repository.model.StudentFeeHistoryInfo
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.Homescreen
import prajna.app.ui.adapter.EventsAdapter
import prajna.app.ui.adapter.FeeHistoryAdapter
import prajna.app.utills.customFonts.Button_Normal
import prajna.app.utills.customFonts.EditeText_font
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class EventsActivity : AppCompatActivity() {

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

    private lateinit var rv_assignment: RecyclerView
    private lateinit var tvEmpty: Text_Medium
    private val feehistoryList: ArrayList<EventsInfo> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.events_layout)
        mTokenData = UserSession(this@EventsActivity).getStudentKey()

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mClassCode = dict_filterdata.getString("stud_classcode")


        } catch (e: Exception) {
            e.printStackTrace()
        }

        initialise()


    }

    private fun initialise() {
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)
        rv_assignment = findViewById(R.id.rv_fees_history)
        tvEmpty = findViewById(R.id.tvEmpty)
        ivBack.visibility = View.VISIBLE



        val layoutmanager = LinearLayoutManager(this@EventsActivity)
        rv_assignment.layoutManager = layoutmanager
        rv_assignment.isNestedScrollingEnabled = false
        ivBack.visibility=View.VISIBLE
        tvHeadertitle.text = "Events"

        getEventsData()

        llback.setOnClickListener(View.OnClickListener {
           onBackPressed()
        })

    }
    private fun validate(): Boolean {
        var valid = true

        if (mFeedback.isEmpty()) {
//            etUsername.error = "Please enter Username"
            Toast.makeText(this@EventsActivity, "Please enter Feedback", Toast.LENGTH_SHORT).show()
            valid = false
        }
        return valid
    }


    private fun getEventsData() {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@EventsActivity).getRetrofit()!!.create()
        retrofitService.getEvents(mClassCode).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    val string = response.body()!!.string()
                    Log.e("TAG", "hallticket**** : " + string)

                    val jsonar= JSONArray(string)

                    if(jsonar.length()>0){
                        for (i in 0 until jsonar.length()){
                            val jsonobj = jsonar.getJSONObject(i)
                            val EventName=jsonobj.getString("EventName").toString()
                            val StartDate=jsonobj.getString("StartDate").toString()
                            val EndDate=jsonobj.getString("EndDate").toString()
                            val OrganiserName=jsonobj.getString("OrganiserName").toString()

                            feehistoryList.add(EventsInfo(EventName,StartDate,EndDate,OrganiserName))


                        }
                        val assignmentAdapter = EventsAdapter(this@EventsActivity, feehistoryList)
                        rv_assignment.adapter = assignmentAdapter
                        rv_assignment.visibility=View.VISIBLE
                        tvEmpty.visibility=View.GONE
                    }else{
                        rv_assignment.visibility=View.GONE
                        tvEmpty.visibility=View.VISIBLE
                    }



                }else{
                    rv_assignment.visibility=View.GONE
                    tvEmpty.visibility=View.VISIBLE
                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@EventsActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Homescreen::class.java))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }
}
