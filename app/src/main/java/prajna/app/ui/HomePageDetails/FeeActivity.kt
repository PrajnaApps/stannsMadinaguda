//package prajna.app.ui.HomePageDetails
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.view.View.OnClickListener
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.atom.mpsdklibrary.PayActivity
//import com.google.gson.JsonObject
//import com.google.gson.JsonParser
//import com.qlx.myviejas.utills.sharedPreferences.UserSession
//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64
//import okhttp3.ResponseBody
//import org.json.JSONArray
//import org.json.JSONObject
//import prajna.app.R
//import prajna.app.repository.api.ApiServices
//import prajna.app.repository.model.StudentFeeInfo
//import prajna.app.repository.retrofitservice.ServiceBuilder
//import prajna.app.ui.Homescreen
//import prajna.app.ui.adapter.StudentFeeAdapter
//import prajna.app.utills.customFonts.Text_Bold
//import prajna.app.utills.customFonts.Text_Medium
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.create
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//class FeeActivity : AppCompatActivity(), OnClickListener {
//
//    private var mTotalAmount: String=""
//    private var mPayAmount: String=""
//    private var mFeeTypeKey: Int=0
//    private var mPaymentModeKey: Int=0
//    private var mTerm: String=""
//    private var newString: String = ""
//    private var mAdmission: String = ""
//    private var mYearCode: String = ""
//    private var mClassCode: String = ""
//    private var mStudentKey: String = ""
//    private var mTokenData: String = ""
//    private var mPhone: String = ""
//    private var mFatherName: String = ""
//    private var mCurrentDay: String = ""
//    private var mOrderID: String = ""
//    private var mFinalOrderID: String = ""
//
//
//    private lateinit var llBack: LinearLayout
//    private lateinit var ivBack: ImageView
//    private lateinit var tvHeadertitle: Text_Bold
//    private lateinit var rv_fees: RecyclerView
//    private lateinit var tvEmpty: Text_Medium
//
//    private val studentFeeList: ArrayList<StudentFeeInfo> = ArrayList()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fee_layout)
//
//        mTokenData = UserSession(this@FeeActivity).getStudentKey()
//
//        var dict_filterdata = JSONObject()
//        try {
//            dict_filterdata = JSONObject(mTokenData)
//            mStudentKey = dict_filterdata.getString("stud_key")
//            mYearCode = dict_filterdata.getString("stud_yearcode")
//            mAdmission = dict_filterdata.getString("stud_admissionNo")
//            mClassCode = dict_filterdata.getString("stud_classcode")
//            mFatherName = dict_filterdata.getString("stud_father")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//
////        01/10/2019 18:31:00
//        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
//        mCurrentDay = sdf.format(Date())
//        Log.e("TAG", "Day___: " + mCurrentDay)
//
//        initView()
//
//
//        val dict_json = JSONObject()
//        val jsonParser = JsonParser()
//        if (!mStudentKey.equals("")) {
//            dict_json.put("YearCode", mYearCode)
//            dict_json.put("ClassKey", mClassCode)
//            dict_json.put("AdmissionNo", mAdmission)
//            val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
//            Log.e("TAG", "exper_json: " + gsonObject)
//            getFeeData(gsonObject)
//        }
//
//
//    }
//
//    fun payNow(mTerm_type:Int,mBalanceAmt:Int,mStudentKey:Int,mFeeTeKey:Int,mPayModeKey:Int) {
//
//        mFeeTypeKey=mFeeTeKey
//        mPaymentModeKey=mPayModeKey
//        mPayAmount=mBalanceAmt.toString()
//        mTerm=mTerm_type.toString()
//         mTotalAmount=mBalanceAmt.toString()+".00"
//        Log.e("TAG", "payNow: "+mTotalAmount )
//
//        val dict_json = JSONObject()
//        val jsonParser = JsonParser()
//        dict_json.put("StudentFKey", mStudentKey)
//        dict_json.put("PayingAmount", mPayAmount)
//        dict_json.put("Purpose", "Online API Test")
//        dict_json.put("Term", mTerm)
//        dict_json.put("FeeTypeKey", mFeeTypeKey.toString())
//        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
//        Log.e("TAG", "Fee_transaction***: " + gsonObject)
//
//        getFeeTransaction(gsonObject)
//
//
//    }
//
//    fun encodeBase64(encode: String): String? {
//        println("[encodeBase64] Base64 encode : $encode")
//        var decode: String? = null
//        try {
//            decode = Base64.encode(encode.toByteArray())
//        } catch (e: Exception) {
//            println("Unable to decode : $e")
//        }
//        return decode
//    }
//
//    /*FeeTransaction*/
//
//
//
//    private fun initView() {
//        llBack = findViewById(R.id.llback)
//        ivBack = findViewById(R.id.ivBack)
//        tvHeadertitle = findViewById(R.id.tvHeadertitle)
//        tvHeadertitle.setText("Fee Details")
//
//
//        rv_fees = findViewById(R.id.rv_fees)
//        tvEmpty = findViewById(R.id.tvEmpty)
//        ivBack.visibility = View.VISIBLE
//
//
//        val layoutmanager = LinearLayoutManager(this@FeeActivity)
//        rv_fees.layoutManager = layoutmanager
//        rv_fees.isNestedScrollingEnabled = false
//
////        tvStudentName = findViewById(R.id.tvStudentName)
////        tvMobile = findViewById(R.id.tvMobile)
////        tvPaidAmount = findViewById(R.id.tvPaidAmount)
////        tvAmount = findViewById(R.id.tvAmount)
////        tvTransactionDate = findViewById(R.id.tvTransactionDate)
////        tvPaymentStatus = findViewById(R.id.tvPaymentStatus)
////        tvPaymentMode = findViewById(R.id.tvPaymentMode)
////        btPay = findViewById(R.id.btPay)
//        ivBack.visibility = View.VISIBLE
//
//        // Initialize Atompayment in onCreate method else payment gateway will throw error.
//
//
//    }
//
//    override fun onClick(v: View) {
//        val i = v.id
//        if (i == R.id.llback) {
//            onBackPressed()
//
//        }
//    }
//
//    private fun getFeeData(gsonObject: JsonObject) {
//
////        pbar.visibility = View.VISIBLE
//        val retrofitService: ApiServices =
//            ServiceBuilder(this@FeeActivity).getRetrofit()!!.create()
//        retrofitService.getFeeDetails(gsonObject).enqueue(object : Callback<ResponseBody> {
//
//            override fun onResponse(
//                call: Call<ResponseBody>, response: Response<ResponseBody>
//            ) {
//                val string = response.body()!!.string()
//                Log.e("TAG", "onResponse: " + string)
//
////                Log.e("TAG", "onResponse() called with: call = [" + call.request().url + "], response = [" + string + "]");
//
////                val dataobj = jsonarray.getJSONObject(0)
//
//                    val jsonar = JSONArray(string)
//                Log.e("TAG", "array: "+jsonar.length() )
//                if (jsonar.length()>0) {
//                    for (i in 0 until jsonar.length()) {
//                        val jsonobj = jsonar.getJSONObject(i)
//                        val FeeTypePkey = jsonobj.getString("FeeTypePkey")
//                        val FeeTypeDescription = jsonobj.getString("FeeTypeDescription")
//                        val PaymentModePKey = jsonobj.getString("PaymentModePKey")
//                        val Amount = jsonobj.getString("Amount").toFloat()
//                        val Term = jsonobj.getInt("Term")
//                        val StudentFkey = jsonobj.getString("StudentFkey").toInt()
//                        val PaidAmount = jsonobj.getString("PaidAmount").toFloat()
//                        val Due = jsonobj.getString("Due").toFloat()
//                        val CreatedDate = jsonobj.getString("CreatedDate")
//
//
//                        studentFeeList.add(
//                            StudentFeeInfo(FeeTypePkey.toInt(),FeeTypeDescription, PaymentModePKey.toInt(),Amount, Term, StudentFkey, PaidAmount, Due, CreatedDate))
//
//
//                    }
//                    val assignmentAdapter = StudentFeeAdapter(this@FeeActivity, studentFeeList)
//                    rv_fees.adapter = assignmentAdapter
//                    rv_fees.visibility = View.VISIBLE
//                    tvEmpty.visibility = View.GONE
//
//                } else {
//                    rv_fees.visibility = View.GONE
//                    tvEmpty.visibility = View.VISIBLE
//
//                }
//
//
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(this@FeeActivity, t.message, Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//
//    private fun paymentFun() {
//
//        val newPayIntent = Intent(this, PayActivity::class.java);
//        newPayIntent.putExtra("isLive", false); //true for Live
//        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
//        newPayIntent.putExtra("merchantId", "197");
//        newPayIntent.putExtra("loginid", "197");
//        newPayIntent.putExtra("password", "Test@123");//NCA@1234
//        newPayIntent.putExtra("prodid", "NSE");//NCA
//        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
//        newPayIntent.putExtra("clientcode", encodeBase64("007"));
//        newPayIntent.putExtra("custacc", "100000036600");
//        newPayIntent.putExtra("channelid", "INT");
//        newPayIntent.putExtra("amt", mTotalAmount); //Should be 2 decimal number i.e 1.00
//        newPayIntent.putExtra("txnid", "013"); //013
//        newPayIntent.putExtra("date", mCurrentDay);//Should be in same format
//        newPayIntent.putExtra("signature_request", "KEY123657234");
//        newPayIntent.putExtra("signature_response", "KEYRESP123657234");
//        newPayIntent.putExtra("discriminator", "All");//NB,All
//        newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param");
////Optinal Parameters
//        newPayIntent.putExtra("customerName", mFatherName);//Only for Name
//        newPayIntent.putExtra("customerEmailID", "Test@gmail.com");//Only for Email ID
//        newPayIntent.putExtra("customerMobileNo", mPhone);//Only for Mobile No.
//        newPayIntent.putExtra("billingAddress", "Hyderabad");//Only for Address
//        newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 2");// Can pass any data
//// Pass data in XML format, only for Multi product
//        startActivityForResult(newPayIntent, 1);
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // check if the request code is same as what is passed here it is 2
//        println("RESULTCODE--->$resultCode")
//        println("REQUESTCODE--->$requestCode")
//        println("RESULT_OK--->" + Activity.RESULT_OK)
//
//
//        if (requestCode == 1) {
//            println("---------INSIDE-------")
//            if (data != null) {
//                Log.e("TAG", "response*** : " + data)
//                val message = data.getStringExtra("status")
//                val resKey = data.getStringArrayExtra("responseKeyArray")
//                val resValue = data.getStringArrayExtra("responseValueArray")
//
//                val payment_json = JSONObject()
//                val payment_parser = JsonParser()
//
//                if (resKey != null && resValue != null) {
//                    for (i in resKey.indices) {
////                        if(resKey[i].equals("udf4"))
//                        payment_json.put(resKey[i], resValue[i])
//
//                    }
//                    payment_json.put("term_type", mTerm)
////                    mOrderID.replace("\"", "");
//
//                    payment_json.put("udf5", mFinalOrderID)
//                    payment_json.put("udf6", mStudentKey)
//                    payment_json.put("udf10", mYearCode)
//                    if (payment_json.toString().contains("udf4")) {
//                        val one_String = payment_json.toString().replace("udf4", "city")
//                        val two_String = one_String.toString().replace("udf5", "OrderId")
//                        val three_String = two_String.toString().replace("udf6", "StudentFkey")
//                        val four_String = three_String.toString().replace("udf10", "Yearcode")
//                        newString = four_String.replace("udf3", "phone")
//
//                    }
//                    Log.e("TAG", "payment_Respo: " + newString)
//                    val gsonObject = payment_parser.parse(newString.toString()) as JsonObject
//                    saveStudentFeeData(gsonObject)
//
//
////                        Log.e("TAG", "payment_Respo: "+"  " + i + " resKey : " + resKey[i] + " resValue : " + resValue[i] )
//                }
//                println("RECEIVED BACK--->$message")
//            }
//        }
//
//    }
//
//
//
//    private fun saveStudentFeeData(gsonObject: JsonObject) {
//
////        pbar.visibility = View.VISIBLE
//        val retrofitService: ApiServices =
//            ServiceBuilder(this@FeeActivity).getRetrofit()!!.create()
//        retrofitService.getFeeDetails(gsonObject).enqueue(object : Callback<ResponseBody> {
//
//            override fun onResponse(
//                call: Call<ResponseBody>, response: Response<ResponseBody>
//            ) {
//                val string = response.body()!!.string()
//
//                val dict_json = JSONObject()
//                val jsonParser = JsonParser()
//                dict_json.put("StudentFKey", mStudentKey)
//                dict_json.put("PayingAmount", mPayAmount)
//                dict_json.put("Yearcode", mYearCode)
//                dict_json.put("ClassKey", mClassCode)
//                dict_json.put("PaymentModeKey",2)
//                dict_json.put("Term", mTerm)
//                dict_json.put("FeeTypePkey", mFeeTypeKey.toString())
//                val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
//                Log.e("TAG", "Fee_transaction***: " + gsonObject)
//                getFeeSuccess(gsonObject)
//
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(this@FeeActivity, t.message, Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//    private fun getFeeSuccess(gsonObject: JsonObject) {
//
////        pbar.visibility = View.VISIBLE
//        val retrofitService: ApiServices =
//            ServiceBuilder(this@FeeActivity).getRetrofit()!!.create()
//        retrofitService.saveStudentFeeCollection(gsonObject).enqueue(object : Callback<ResponseBody> {
//
//            override fun onResponse(
//                call: Call<ResponseBody>, response: Response<ResponseBody>
//            ) {
//                val string = response.body()!!.string()
//                finish();
//                startActivity(getIntent());
//
//                Toast.makeText(this@FeeActivity, "TRANSACTION IS SUCCESSFUL", Toast.LENGTH_LONG)
//                    .show()
//
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(this@FeeActivity, t.message, Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//    private fun getFeeTransaction(gsonObject: JsonObject) {
//
////        pbar.visibility = View.VISIBLE
//        val retrofitService: ApiServices =
//            ServiceBuilder(this@FeeActivity).getRetrofit()!!.create()
//        retrofitService.saveStudentFeePre(gsonObject).enqueue(object : Callback<ResponseBody> {
//
//            override fun onResponse(
//                call: Call<ResponseBody>, response: Response<ResponseBody>
//            ) {
//                val string = response.body()!!.string()
//                mOrderID=string
//                mFinalOrderID = mOrderID.replace("\"", "")
//
//                Log.e("TAG", "order_ ** : "+mFinalOrderID )
//
//                paymentFun()
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(this@FeeActivity, t.message, Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//
//    override fun onBackPressed() {
//
//        startActivity(Intent(this@FeeActivity, Homescreen::class.java))
//        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
//        finish()
//        super.onBackPressed()
//    }
//
//
//}