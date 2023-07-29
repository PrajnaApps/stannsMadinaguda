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
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.Progressreport
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.Homescreen
import prajna.app.ui.adapter.ProgressReportAdapter
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create


class ProgressReport_Activity :AppCompatActivity() {


//    private lateinit var resultArray: JSONArray
    private var mTokenData: String=""
    private var mStudentKey: String=""
    private var mYearCode: String=""
    private var mClassCode: String=""
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var rv_assignment: RecyclerView
    private lateinit var tvEmpty: Text_Medium
    private val subjectList: ArrayList<Progressreport> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paymenthistory)

        mTokenData = UserSession(this@ProgressReport_Activity).getStudentKey()

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")
            mYearCode = dict_filterdata.getString("stud_yearcode")
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
        tvHeadertitle.setText("Progress Report")
        ivBack.visibility = View.VISIBLE



        val layoutmanager = LinearLayoutManager(this@ProgressReport_Activity)
        rv_assignment.layoutManager = layoutmanager
        rv_assignment.isNestedScrollingEnabled = false

        llback.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        val dict_json = JSONObject()
        val jsonParser = JsonParser()
        dict_json.put("StudentPKey", mStudentKey)
        dict_json.put("ClassKey", mClassCode)
        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
        Log.e("TAG", "exper_json: " + gsonObject)

        getProgressReport(gsonObject)


    }

    private fun getProgressReport(hallticket: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@ProgressReport_Activity).getRetrofit()!!.create()
        retrofitService.getPrgressCard(hallticket).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                val resultArray = JSONArray()

                if (response.code() == 200) {
                    val string = response.body()!!.string()

                    val jsonar= JSONArray(string)

                    Log.e("TAG", "hallticket**** : " + jsonar)

                    val groupedJson: MutableMap<String?, MutableList<JSONObject?>> = HashMap()
                    for (i in 0 until jsonar!!.length()) {
                        var obj: JSONObject? = null
                        var examName: String? = null
                        try {
                            obj = jsonar.getJSONObject(i)
                            examName = obj.getString("ExamName")
                        } catch (e: JSONException) {
                            throw RuntimeException(e)
                        }
                        if (groupedJson.containsKey(examName)) {
                            groupedJson[examName]!!.add(obj)
                        } else {
                            val list: MutableList<JSONObject?> = ArrayList()
                            list.add(obj)
                            groupedJson[examName] = list
                        }
                    }
                    for (examName in groupedJson.keys) {
                        val groupObj = JSONObject()
                        try {
                            groupObj.put("ExamName", examName)
                            groupObj.put("Subjects", groupedJson[examName])
                            resultArray.put(groupObj)
                        } catch (e: JSONException) {
                            throw RuntimeException(e)
                        }
                    }
                    val groupedJsonString = resultArray.toString()
                    println(groupedJsonString)
                    Log.e("TAG", "final_test: ${groupedJsonString.trim()}")
                }
                    if(resultArray.length()>0){
                        for (i in 0 until resultArray.length()){
                            val jsonobj = resultArray.getJSONObject(i)
                            val ExamName = jsonobj.getString("ExamName")
                            val Subjects = jsonobj.getString("Subjects")

                            subjectList.add(Progressreport(ExamName,Subjects))

                        }
                        val assignmentAdapter = ProgressReportAdapter(this@ProgressReport_Activity, subjectList)
                        rv_assignment.adapter = assignmentAdapter
                        rv_assignment.visibility=View.VISIBLE
                        tvEmpty.visibility=View.GONE
                    }else{
                        rv_assignment.visibility=View.GONE
                        tvEmpty.visibility=View.VISIBLE
                    }





            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ProgressReport_Activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ProgressReport_Activity, Homescreen::class.java))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }

}