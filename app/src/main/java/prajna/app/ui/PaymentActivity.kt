package prajna.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.atom.sdk.AtomConfiguration
import com.atom.sdk.AtomPayment
import com.atom.sdk.OnPaymentResponseListener
import com.atom.sdk.model.common.CustDetails
import com.atom.sdk.model.request.PaymentData
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.retrofitservice.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create


class PaymentActivity :AppCompatActivity(), OnPaymentResponseListener {



//    private var mTotalAmount: String = ""
    private var mPayAmount: Int = 0
    private var mTotalAmount: String = ""
    private var mFeeTypePKey: Int = 0
    private var mFeeTypeFKey: Int = 0
    private var mPaymentModeKey: Int = 0
    private var mTerm: Int = 0


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
    private lateinit var mPb: ProgressBar


    /*Atom Payment*/
    lateinit var atom: AtomPayment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.atom_payscreen)

        mPb=findViewById(R.id.pb);

        atom = AtomPayment(this@PaymentActivity)
        val config = AtomConfiguration.getInstance()
        config.baseUrl =
            "https://caller.atomtech.in/"
        config.requestCipherKey =
            "A4476C2062FFA58980DC8F79EB6A799E"
        config.requestCipherSalt =
            "A4476C2062FFA58980DC8F79EB6A799E"
        config.requestSignatureHashKey = "KEY123657234"
        config.responseCipherKey =
            "75AEF0FA1B94B3C10D4F5B268F757F11"
        config.responseCipherSalt =
            "75AEF0FA1B94B3C10D4F5B268F757F11"
        config.responseSignatureHashKey = "KEYRESP123657234"
        config.merchId = 317157
        config.password = "Test@123"
        atom!!.setConfiguration(config)
        atom!!.setResponseListener(this)

        mTokenData = UserSession(this@PaymentActivity).getStudentKey()


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



        Log.e("TAG", "payNow: " + intent.getIntExtra("pay_term",0))

        val dict_json = JSONObject()
        val jsonParser = JsonParser()

        mFeeTypePKey = intent.getIntExtra("pay_studPkey",0)
        mFeeTypeFKey = intent.getIntExtra("pay_studFkey",0)
        mPaymentModeKey = intent.getIntExtra("pay_studModePkey",0)
        mTerm = intent.getIntExtra("pay_term",0)
        mTotalAmount =  intent.getIntExtra("pay_balnce",0).toString()+ ".00"
        mPayAmount =  intent.getIntExtra("pay_balnce",0)

//        mFeeTypeKey = mFeeTeKey
//        mPaymentModeKey = mPayModeKey
//        mPayAmount = mBalanceAmt.toString()
//        mTerm = mTerm_type.toString()
//        mTotalAmount = mBalanceAmt.toString() + ".00"
//        Log.e("TAG", "payNow: " + mTotalAmount)
//
//        val dict_json = JSONObject()
//        val jsonParser = JsonParser()
        dict_json.put("StudentFKey", mStudentKey)
        dict_json.put("PayingAmount", mPayAmount)
        dict_json.put("Purpose", "Online API Test")
        dict_json.put("Term", mTerm)
        dict_json.put("FeeTypeKey", mFeeTypePKey.toString())
        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
        Log.e("TAG", "Fee_transaction***: " + gsonObject)

        getFeeTransaction(gsonObject)


    }



    private fun buildPaymentData() {
        mPb.visibility=View.GONE
        val builder = PaymentData.Builder()
        builder.setMerchantId(317157)
            .setPassword("Test@123")
            .setVersion("OTSv1.1")
            .setPayMode("RD")
            .setChannel("ECOMM")
            .setApi("SALE")
            .setStage(1)
            .setPlatform("WEB")
            .setMerchTxnId("1234567890")
            .setMerchType("R")
            .setMccCode(5999) //5999 6211
            .setAmount(mTotalAmount.toDouble())
            .setSurchargeAmount(0.0)
            .setTotalAmount(mTotalAmount.toDouble()) // (amount + surcharge)
            .setCustAccNo("123456789012")
            .setCustAccIfsc("DLXB0000092")
            .setClientCode("12345")
            .setTxnCurrency("INR")
            .setReturnUrl("https://pgtest.atomtech.in/mobilesdk/param")
            .setCustDetails(
                CustDetails(
                    mFatherName,
                    null,
                    "test@gmail.com",
                    mPhone,
                    null
                )
            )
            .addProdDetails("NSE", 1f)
        val data = builder.build()
        try {
            // To initiate the payment flow
            atom!!.startPaymentProcess(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun OnPaymentResponse(data: String) {
        Log.e("TAG", "pay*** res: "+data )
        val payment_parser = JsonParser()

        if (data != null) {
            println("Response:$data")
            var obj: JSONObject
            var desc: String=""
            try {
                obj = JSONObject(data)
                val payInst = obj.getJSONObject("payInstrument")

                val mainObject = JSONObject(payInst.toString())

                val custDetailsObj: JSONObject = mainObject.getJSONObject("custDetails")
                custDetailsObj.put("city", "Hyderabad");
                custDetailsObj.put("OrderId", mFinalOrderID);
                custDetailsObj.put("StudentFkey", mStudentKey);
                custDetailsObj.put("Yearcode", mYearCode);
                val updatedJsonString = mainObject.toString()
               val newString = updatedJsonString.replace("custMobile", "phone")

                Log.e("TAG", "updatedJsonString: "+newString )

                val gsonObject = payment_parser.parse(newString) as JsonObject

                val payment_json = JSONObject()

                payment_json.put("OrderId", mFinalOrderID)
                payment_json.put("amt", mTotalAmount)

                Log.e("TAG", "payment_newre: " + payment_json)

                UserSession(this@PaymentActivity).setPaymentData(payment_json.toString())

                desc = payInst.getJSONObject("responseDetails").getString("description")
                Log.e("TAG", "pay_response: "+desc )

                if(payInst.getJSONObject("responseDetails").getString("message").equals("FAILED")){
                    UserSession(this@PaymentActivity).removePaymentData()
                    startActivity(Intent(this@PaymentActivity, PaymentSuccessActivity::class.java)
                        .putExtra("id", "fail"))

                    Toast.makeText(this@PaymentActivity, "Failed Payment", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    Log.e("TAG", "save_response12: "+gsonObject )

                    saveStudentFeeData(gsonObject)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Toast.makeText(this@PaymentActivity, desc, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@PaymentActivity, "Transaction Cancelled", Toast.LENGTH_LONG).show()
        }
    }

    private fun getFeeTransaction(gsonObject: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@PaymentActivity).getRetrofit()!!.create()
        retrofitService.saveStudentFeePre(gsonObject).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                val string = response.body()!!.string()
                mOrderID = string
                mFinalOrderID = mOrderID.replace("\"", "")

                Log.e("TAG", "order_ ** : " + mFinalOrderID)

                buildPaymentData();
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@PaymentActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun saveStudentFeeData(gsonObject: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@PaymentActivity).getRetrofit()!!.create()
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
                dict_json.put("FeeTypePkey", mFeeTypePKey)
                val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
                Log.e("TAG", "Fee_success_faile***: " + gsonObject)
                getFeeSuccess(gsonObject)

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@PaymentActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun getFeeSuccess(gsonObject: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@PaymentActivity).getRetrofit()!!.create()
        retrofitService.saveStudentFeeCollection(gsonObject).enqueue(object :
            Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                startActivity(
                    Intent(
                        this@PaymentActivity,
                        PaymentSuccessActivity::class.java
                    ).putExtra("id", "success")
                )

                Toast.makeText(this@PaymentActivity, "TRANSACTION IS SUCCESSFUL", Toast.LENGTH_LONG)
                    .show()

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@PaymentActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
    }




}