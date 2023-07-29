package prajna.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.model.StudentFeeInfo
import prajna.app.ui.PaymentActivity
import prajna.app.ui.WebviewFee
import prajna.app.utills.customFonts.Button_Bold
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Normal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class StudentFeeAdapter(var context: Context, var dataList: ArrayList<StudentFeeInfo>) :
    RecyclerView.Adapter<StudentFeeAdapter.ViewHolder>() {


//    var listner = listner


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTerm: Text_Bold = view.findViewById(R.id.tvTerm)
        var tvPaidAmount: Text_Normal = view.findViewById(R.id.tvPaidAmount)
        var tvDueAmt: Text_Normal = view.findViewById(R.id.tvDueAmt)
        var tvDate: Text_Normal = view.findViewById(R.id.tvDate)
        var btPay: Button_Bold = view.findViewById(R.id.btPay)
        var hidden_view: LinearLayout = view.findViewById(R.id.hidden_view)
//        var ivSHow: ImageView = view.findViewById(R.id.ivSHow)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.student_fee_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val data = dataList[position]
//

        val from_date = data.CreatedDate
        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val dt1 = format1.parse(from_date)

        val from_month: DateFormat = SimpleDateFormat("MMM")
        val from_year: DateFormat = SimpleDateFormat("yyyy")
        val from_day: DateFormat = SimpleDateFormat("dd")
        val mMonth: String = from_month.format(dt1)
        val mYear: String = from_year.format(dt1)
        val mDay: String = from_day.format(dt1)
        holder.tvDate.setText(mMonth + " " + mDay + ", " + mYear)

        holder.tvPaidAmount.setText("Paid Amount: Rs " + data.PaidAmount.toString())
        holder.tvDueAmt.setText("Due Amount: Rs " + data.Due.toString())
        holder.tvTerm.setText("TERM " + data.Term)

//        Log.e("TAG", "inner_posout** : "+position )
        Log.e("TAG", "inner_** : " + data.Term)

        if (data.Due.toInt() == 0) {
            holder.btPay.visibility = View.GONE
        } else {
            holder.btPay.visibility = View.VISIBLE
        }

        holder.btPay.setOnClickListener(View.OnClickListener {
            val data = dataList[position]
            val token_json = JSONObject()

            if (dataList.size > 0) {
                if (position == 0) {
                    val mBalance = data.Amount.toInt() - data.PaidAmount.toInt()
                    Log.e("TAG", "balance_amt " + mBalance)

                    val newPayIntent = Intent(context, WebviewFee::class.java);
//                        newPayIntent.putExtra("pay_term",data.Term)
//                        newPayIntent .putExtra("pay_balnce", mBalance)
//                        newPayIntent .putExtra("pay_studFkey", data.StudentFkey)
//                        newPayIntent .putExtra("pay_studPkey", data.FeeTypePkey)
//                        newPayIntent .putExtra("pay_studModePkey", data.PaymentModePKey)
                    context.startActivity(newPayIntent)



                } else {
                    val mPos = position
                    val nPos = mPos - 1
                    val data1 = dataList[nPos]

                    if (data1.Due.toInt() == 0) {
                        val mBalance = data.Amount.toInt() - data.PaidAmount.toInt()
                        Log.e("TAG", "balance_amt " + mBalance)

                        val newPayIntent = Intent(context, WebviewFee::class.java);
//                        newPayIntent.putExtra("pay_term",data.Term)
//                        newPayIntent .putExtra("pay_balnce", mBalance)
//                        newPayIntent .putExtra("pay_studFkey", data.StudentFkey)
//                        newPayIntent .putExtra("pay_studPkey", data.FeeTypePkey)
//                        newPayIntent .putExtra("pay_studModePkey", data.PaymentModePKey)
                        context.startActivity(newPayIntent)


                    } else {
                        Toast.makeText(
                            context,
                            "Please Pay Previous Term First ",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

            }


//


        })


    }


    override fun getItemCount(): Int {
        return dataList.size
    }

//    interface onFragmentInteractionListner {
//        fun payNow(
//            term: Int,
//            mBalance: Int,
//            studentFkey: Int,
//            feeTypePkey: Int,
//            paymentModePKey: Int
//        )
//    }


}