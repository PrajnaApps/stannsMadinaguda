package prajna.app.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import org.json.JSONObject
import prajna.app.R
import prajna.app.login.LoginActivity
import prajna.app.utills.customFonts.Button_Bold
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium

class PaymentSuccessActivity : AppCompatActivity() {

    private var mResponseData: String = ""
    private var mId: String = ""
    private var mPaidAmt: String = ""
    private var mOrderId: String = ""
    private lateinit var anim_success: LottieAnimationView
    private lateinit var anim_fail: LottieAnimationView
    private lateinit var tvAmount: Text_Medium
    private lateinit var tvPaystatus: Text_Bold
    private lateinit var tvOrderId: Text_Medium
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var btSubmit: Button_Bold

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_success)

        mId = intent.getStringExtra("id").toString()
        mResponseData = UserSession(this@PaymentSuccessActivity).getPaymentData()

        var dict_filterdata = JSONObject()
        if (mResponseData.length > 0) {
            try {
                dict_filterdata = JSONObject(mResponseData)
                mPaidAmt = dict_filterdata.getString("amt")
                mOrderId = dict_filterdata.getString("OrderId")


                Log.e("TAG", "mOrderId*** : " + mOrderId)


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

//        mId="success"


        initView()
    }

    private fun initView() {
        anim_success = findViewById(R.id.anim_success)
        anim_fail = findViewById(R.id.anim_fail)
        tvAmount = findViewById(R.id.tvAmount)
        tvOrderId = findViewById(R.id.tvOrderId)
        tvPaystatus = findViewById(R.id.tvPaystatus)
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)
        btSubmit = findViewById(R.id.btSubmit)
        tvHeadertitle.setText("Payment Confirmation")
        ivBack.visibility = View.VISIBLE


        if (mId.equals("fail")) {
            anim_success.visibility = View.GONE
            anim_fail.visibility = View.VISIBLE
            tvPaystatus.setText("Payment Failed")
            btSubmit.setText("Try Again")
        } else {
            anim_success.visibility = View.VISIBLE
            anim_fail.visibility = View.GONE
            tvPaystatus.setText("Payment Success")

            tvOrderId.setText("Transaction ID :  " + mOrderId)
            tvAmount.setText("Amount Paid : Rs " + mPaidAmt)
            btSubmit.setText("Continue")

        }
        btSubmit.setOnClickListener(View.OnClickListener {

            startActivity(Intent(this@PaymentSuccessActivity, Homescreen::class.java).putExtra("id", "history"))
            overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)

        })

        llback.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@PaymentSuccessActivity, Homescreen::class.java).putExtra("id", "history"))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }
}