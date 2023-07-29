package prajna.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atom.sdk.AtomConfiguration
import com.atom.sdk.AtomPayment
import com.atom.sdk.OnPaymentResponseListener
import com.atom.sdk.model.common.CustDetails
import com.atom.sdk.model.request.PaymentData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.StudentFeeInfo
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.HomePageDetails.SummaryActivity
import prajna.app.ui.adapter.StudentFeeAdapter
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Date


/**
 * A simple [Fragment] subclass.
 */
class FeeFragment : Fragment(){


    private var mTotalAmount: String = ""
    private var mPayAmount: String = ""
    private var mFeeTypeKey: Int = 0
    private var mPaymentModeKey: Int = 0
    private var mTerm: String = ""
    private var newString: String = ""
    private var mAdmission: String = ""
    private var mYearCode: String = ""
    private var mClassCode: String = ""
    private var mStudentKey: String = ""
    private var mTokenData: String = ""
    private var mPhone: String = ""
    private var mFatherName: String = ""
    private var mCurrentDay: String = ""
    private var mOrderID: String = ""
    private var mFinalOrderID: String = ""


    private lateinit var llBack: LinearLayout
    private lateinit var ivBack: ImageView
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var tvLogout: Text_Bold
    private lateinit var rv_fees: RecyclerView
    private lateinit var tvEmpty: Text_Medium



    private val studentFeeList: ArrayList<StudentFeeInfo> = ArrayList()

    /*Atom Payment*/
    lateinit var atom: AtomPayment
    var config: AtomConfiguration? = null
    lateinit var builder: PaymentData.Builder
    lateinit var data: PaymentData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fee_layout, container, false)

        mTokenData = UserSession(requireActivity()).getStudentKey()


        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")
            mYearCode = dict_filterdata.getString("stud_yearcode")
            mAdmission = dict_filterdata.getString("stud_admissionNo")
            mClassCode = dict_filterdata.getString("stud_classcode")
            mFatherName = dict_filterdata.getString("stud_father")
        } catch (e: Exception) {
            e.printStackTrace()
        }


//        01/10/2019 18:31:00
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        mCurrentDay = sdf.format(Date())
        Log.e("TAG", "Day___: " + mCurrentDay)

        initView(rootView)


        val dict_json = JSONObject()
        val jsonParser = JsonParser()
        if (!mStudentKey.equals("")) {
            dict_json.put("YearCode", mYearCode)
            dict_json.put("ClassKey", mClassCode)
            dict_json.put("AdmissionNo", mAdmission)
            val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
            Log.e("TAG", "exper_json: " + gsonObject)
            getFeeData(gsonObject)
        }




        return rootView
    }


    private fun initView(v: View) {
        llBack = v.findViewById(R.id.llback)
        ivBack = v.findViewById(R.id.ivBack)
        tvHeadertitle = v.findViewById(R.id.tvHeadertitle)
        tvLogout = v.findViewById(R.id.tvLogout)
        tvLogout.visibility = View.VISIBLE
        tvHeadertitle.setText("Fee Details")
        tvLogout.setText("Fee History")


        rv_fees = v.findViewById(R.id.rv_fees)
        tvEmpty = v.findViewById(R.id.tvEmpty)
//        ivBack.visibility = View.VISIBLE


        val layoutmanager = LinearLayoutManager(requireActivity())
        rv_fees.layoutManager = layoutmanager
        rv_fees.isNestedScrollingEnabled = false

        tvLogout.setOnClickListener(View.OnClickListener {

            startActivity(Intent(requireActivity(), PaymentHistory::class.java))
            requireActivity().overridePendingTransition(
                R.anim.move_left_enter,
                R.anim.move_left_exit
            )
            requireActivity().finish()
        })

    }



    private fun getFeeData(gsonObject: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(requireActivity()).getRetrofit()!!.create()
        retrofitService.getFeeDetails(gsonObject).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                val string = response.body()!!.string()
                Log.e("TAG", "onResponse: " + string)

//                Log.e("TAG", "onResponse() called with: call = [" + call.request().url + "], response = [" + string + "]");

//                val dataobj = jsonarray.getJSONObject(0)

                val jsonar = JSONArray(string)
                Log.e("TAG", "array: " + jsonar.length())
                if (jsonar.length() > 0) {
                    for (i in 0 until jsonar.length()) {
                        val jsonobj = jsonar.getJSONObject(i)
                        val FeeTypePkey = jsonobj.getString("FeeTypePkey")
                        val FeeTypeDescription = jsonobj.getString("FeeTypeDescription")
                        val PaymentModePKey = jsonobj.getString("PaymentModePKey")
                        val Amount = jsonobj.getString("Amount").toFloat()
                        val Term = jsonobj.getInt("Term")
                        val StudentFkey = jsonobj.getString("StudentFkey").toInt()
                        val PaidAmount = jsonobj.getString("PaidAmount").toFloat()
                        val Due = jsonobj.getString("Due").toFloat()
                        val CreatedDate = jsonobj.getString("CreatedDate")


                        studentFeeList.add(
                            StudentFeeInfo(
                                FeeTypePkey.toInt(),
                                FeeTypeDescription,
                                PaymentModePKey.toInt(),
                                Amount,
                                Term,
                                StudentFkey,
                                PaidAmount,
                                Due,
                                CreatedDate
                            )
                        )


                    }
                    val assignmentAdapter =
                        StudentFeeAdapter(requireActivity(), studentFeeList)
                    rv_fees.adapter = assignmentAdapter
                    rv_fees.visibility = View.VISIBLE
                    tvEmpty.visibility = View.GONE

                } else {
                    rv_fees.visibility = View.GONE
                    tvEmpty.visibility = View.VISIBLE

                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


//    private fun paymentFun() {
////
////        val newPayIntent = Intent(requireActivity(), PayActivity::class.java);
////        newPayIntent.putExtra("isLive", false); //true for Live
////        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
////        newPayIntent.putExtra("merchantId", "197");
////        newPayIntent.putExtra("loginid", "197");
////        newPayIntent.putExtra("password", "Test@123");//NCA@1234
////        newPayIntent.putExtra("prodid", "NSE");//NCA
////        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
////        newPayIntent.putExtra("clientcode", encodeBase64("007"));
////        newPayIntent.putExtra("custacc", "100000036600");
////        newPayIntent.putExtra("channelid", "INT");
////        newPayIntent.putExtra("amt", mTotalAmount); //Should be 2 decimal number i.e 1.00
////        newPayIntent.putExtra("txnid", "013"); //013
////        newPayIntent.putExtra("date", mCurrentDay);//Should be in same format
////        newPayIntent.putExtra("signature_request", "KEY123657234");
////        newPayIntent.putExtra("signature_response", "KEYRESP123657234");
////        newPayIntent.putExtra("discriminator", "All");//NB,All
////        newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param");
//////Optinal Parameters
////        newPayIntent.putExtra("customerName", mFatherName);//Only for Name
////        newPayIntent.putExtra("customerEmailID", "Test@gmail.com");//Only for Email ID
////        newPayIntent.putExtra("customerMobileNo", mPhone);//Only for Mobile No.
////        newPayIntent.putExtra("billingAddress", "Hyderabad");//Only for Address
////        newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 2");// Can pass any data
////// Pass data in XML format, only for Multi product
////        startActivityForResult(newPayIntent, 1);
//
//
//        builder = PaymentData.Builder();
//        builder.setMerchantId(197)
//            .setPassword("Test@123")
//            .setMerchTxnId("013")
//            .setMerchType("merchType")
//            .setMccCode(encodeBase64("007"))
//            .setAmount(mTotalAmount.toDouble())
//            .setSurchargeAmount(0.0)
//            .setTotalAmount(mTotalAmount.toDouble())
//            .setCustAccNo("100000036600")
//            .setCustAccIfsc("custAccIfsc")
//            .setClientCode("007")
//            .setTxnCurrency("INR")
//            .setReturnUrl("https://pgtest.atomtech.in/paynetzclient/ResponseParam.jsp")
//            .setCustDetails(
//                CustDetails(
//                    "mFatherName",
//                    "",
//                    "Test@gmail.com",
//                    mPhone,
//                    "billingDetails"
//                )
//            )
//            .addProdDetails("", mTotalAmount.toFloat())
//
//
//        data = builder.build();
//        try {
//            atom!!.startPaymentProcess(data)
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // check if the request code is same as what is passed here it is 2
//        println("RESULTCODE--->$resultCode")
//        println("REQUESTCODE--->$requestCode")
//        println("RESULT_OK--->" + Activity.RESULT_OK)
//

        if (requestCode == 1) {
            println("---------INSIDE-------")
            if (data != null) {
                Log.e("TAG", "response*** : " + data)
                val message = data.getStringExtra("status")
                val resKey = data.getStringArrayExtra("responseKeyArray")
                val resValue = data.getStringArrayExtra("responseValueArray")

                val payment_json = JSONObject()
                val payment_parser = JsonParser()

                if (resKey != null && resValue != null) {
                    for (i in resKey.indices) {
//                        if(resKey[i].equals("udf4"))
                        payment_json.put(resKey[i], resValue[i])

                    }
                    payment_json.put("term_type", mTerm)
//                    mOrderID.replace("\"", "");

                    payment_json.put("udf5", mFinalOrderID)
                    payment_json.put("udf6", mStudentKey)
                    payment_json.put("udf10", mYearCode)
                    if (payment_json.toString().contains("udf4")) {
                        val one_String = payment_json.toString().replace("udf4", "city")
                        val two_String = one_String.toString().replace("udf5", "OrderId")
                        val three_String = two_String.toString().replace("udf6", "StudentFkey")
                        val four_String = three_String.toString().replace("udf10", "Yearcode")
                        newString = four_String.replace("udf3", "phone")

                    }
                    val gsonObject = payment_parser.parse(newString.toString()) as JsonObject
                    val mObj = JSONObject(newString)
                    val mOrderid = mObj.getString("OrderId")
                    val mAmount = mObj.getString("amt")
                    val mPhone = mObj.getString("phone")

                    val payment_json = JSONObject()
                    payment_json.put("OrderId", mOrderid.toString())
                    payment_json.put("amt", mAmount.toString())

                    Log.e("TAG", "payment_newre: " + payment_json)

                    UserSession(requireActivity()).setPaymentData(payment_json.toString())

                    if (mPhone.equals("null")) {
                        UserSession(requireActivity()).removePaymentData()
                        startActivity(
                            Intent(
                                requireActivity(),
                                PaymentSuccessActivity::class.java
                            ).putExtra(
                                "id", "fail")
                        )

                        Toast.makeText(requireActivity(), "Failed Payment", Toast.LENGTH_SHORT)
                            .show()
                    } else if (mObj.getString("desc").equals("TRANSACTION CANCELLED BY USER...")) {
                        UserSession(requireActivity()).removePaymentData()
                        startActivity(
                            Intent(
                                requireActivity(),
                                PaymentSuccessActivity::class.java
                            ).putExtra("id", "fail")
                        )

                        Toast.makeText(
                            requireActivity(),
                            mObj.getString("desc"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
//                        Toast.makeText(requireActivity(), "Error Payment", Toast.LENGTH_SHORT).show()
                        saveStudentFeeData(gsonObject)
                    }


//                        Log.e("TAG", "payment_Respo: "+"  " + i + " resKey : " + resKey[i] + " resValue : " + resValue[i] )
                }
                println("RECEIVED BACK--->$message")
            } else {
                UserSession(requireActivity()).removePaymentData()
                startActivity(
                    Intent(
                        requireActivity(),
                        PaymentSuccessActivity::class.java
                    ).putExtra("id", "fail")
                )

                Toast.makeText(requireActivity(), "Failed Payment", Toast.LENGTH_SHORT).show()
            }
        } else {
            UserSession(requireActivity()).removePaymentData()
            startActivity(
                Intent(
                    requireActivity(),
                    PaymentSuccessActivity::class.java
                ).putExtra("id", "fail")
            )

            Toast.makeText(requireActivity(), "Failed Payment", Toast.LENGTH_SHORT).show()
        }

    }


    private fun saveStudentFeeData(gsonObject: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(requireActivity()).getRetrofit()!!.create()
        retrofitService.getSavePaymentResponse(gsonObject).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                val string = response.body()!!.string()

                val dict_json = JSONObject()
                val jsonParser = JsonParser()
                dict_json.put("StudentFKey", mStudentKey)
                dict_json.put("PayingAmount", mPayAmount)
                dict_json.put("Yearcode", mYearCode)
                dict_json.put("ClassKey", mClassCode)
                dict_json.put("PaymentModeKey", 2)
                dict_json.put("Term", mTerm)
                dict_json.put("FeeTypePkey", mFeeTypeKey.toString())
                val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
                Log.e("TAG", "Fee_success_faile***: " + gsonObject)
                getFeeSuccess(gsonObject)

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getFeeSuccess(gsonObject: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(requireActivity()).getRetrofit()!!.create()
        retrofitService.saveStudentFeeCollection(gsonObject).enqueue(object :
            Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
//                val string = response.body()!!.string()
//                requireActivity().finish();
//                startActivity(requireActivity().getIntent());
                startActivity(
                    Intent(
                        requireActivity(),
                        PaymentSuccessActivity::class.java
                    ).putExtra("id", "success")
                )

                Toast.makeText(requireActivity(), "TRANSACTION IS SUCCESSFUL", Toast.LENGTH_LONG)
                    .show()

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


//    fun encodeBase64(encode: String): Int {
//        println("[encodeBase64] Base64 encode : $encode")
//        var decode: String? = null
//        try {
//            decode = Base64.encode(encode.toByteArray())
//        } catch (e: Exception) {
//            println("Unable to decode : $e")
//        }
//        return decode
//    }


//    override fun payNow(
//        mTerm_type: Int,
//        mBalanceAmt: Int,
//        mStudentKey: Int,
//        mFeeTeKey: Int,
//        mPayModeKey: Int
//    ) {
//        mFeeTypeKey = mFeeTeKey
//        mPaymentModeKey = mPayModeKey
//        mPayAmount = mBalanceAmt.toString()
//        mTerm = mTerm_type.toString()
//        mTotalAmount = mBalanceAmt.toString() + ".00"
//        Log.e("TAG", "payNow: " + mTotalAmount)
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
////        getFeeTransaction(gsonObject)
//
//
//    }



}
