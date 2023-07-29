package prajna.app.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.model.InfoDetails
import prajna.app.repository.model.StudentFeeHistoryInfo
import prajna.app.utills.customFonts.Text_Normal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FeeHistoryAdapter(var context: Context, var dataList: ArrayList<StudentFeeHistoryInfo>) :
    RecyclerView.Adapter<FeeHistoryAdapter.ViewHolder>() {


    private var mTokenData: String=""

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvAmt: Text_Normal = view.findViewById(R.id.tvAmt)
        var tvReceipt: Text_Normal = view.findViewById(R.id.tvReceipt)
        var tvDate: Text_Normal = view.findViewById(R.id.tvDate)
        var tvPaymode: Text_Normal = view.findViewById(R.id.tvPaymode)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.feehistory_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
//        mTokenData = UserSession(context).getStudentKey()


        holder.tvAmt.setText("Amount Paid - Rs "+data.PaidAmount)
        holder.tvReceipt.setText("Receipt No. - "+data.ReceiptNo)
        holder.tvPaymode.setText("Payment Mode - "+data.PaymentMode)

//        holder.tvAssignment_year.setText(data.YearCode)
        val assignment_date = data.TransactionDate

        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val dt1 = format1.parse(assignment_date)


        val from_month: DateFormat = SimpleDateFormat("MMM")
        val from_year: DateFormat = SimpleDateFormat("yyyy")
        val from_day: DateFormat = SimpleDateFormat("dd")

        val mMonth: String = from_month.format(dt1)
        val mYear: String = from_year.format(dt1)
        val mDay: String = from_day.format(dt1)

        holder.tvDate.setText(mMonth + " " + mDay + ", " + mYear)



    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}