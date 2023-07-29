package prajna.app.ui

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
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.InfoDetails
import prajna.app.repository.model.StudentFeeHistoryInfo
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.adapter.AssignementAdapter
import prajna.app.ui.adapter.FeeHistoryAdapter
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class PaymentHistory :AppCompatActivity() {


    private var mTokenData: String=""
    private var mStudentKey: String=""
    private var mYearCode: String=""
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var rv_assignment: RecyclerView
    private lateinit var tvEmpty: Text_Medium
    private val feehistoryList: ArrayList<StudentFeeHistoryInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paymenthistory)

        mTokenData = UserSession(this@PaymentHistory).getStudentKey()

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")
            mYearCode = dict_filterdata.getString("stud_yearcode")


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
        tvHeadertitle.setText("Fee History")
        ivBack.visibility = View.VISIBLE



        val layoutmanager = LinearLayoutManager(this@PaymentHistory)
        rv_assignment.layoutManager = layoutmanager
        rv_assignment.isNestedScrollingEnabled = false

        llback.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        val dict_json = JSONObject()
        val jsonParser = JsonParser()
        dict_json.put("StudentPKey", mStudentKey)
        dict_json.put("YearCode", mYearCode)
        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
        Log.e("TAG", "exper_json: " + gsonObject)

        getFeeHistory(gsonObject)


    }

    private fun getFeeHistory(hallticket: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@PaymentHistory).getRetrofit()!!.create()
        retrofitService.getfeeHistory(hallticket).enqueue(object : Callback<ResponseBody> {

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
                            val PaidAmount=jsonobj.getString("PaidAmount").toInt()
                            val ReceiptNo=jsonobj.getString("ReceiptNo").toInt()
                            val PaymentMode=jsonobj.getString("PaymentMode").toString()
                            val TransactionDate=jsonobj.getString("TransactionDate").toString()

                            feehistoryList.add(StudentFeeHistoryInfo(PaidAmount,PaymentMode,ReceiptNo,TransactionDate))


                        }
                        val assignmentAdapter = FeeHistoryAdapter(this@PaymentHistory, feehistoryList)
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
                Toast.makeText(this@PaymentHistory, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@PaymentHistory, Homescreen::class.java).putExtra("id","history"))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }

}