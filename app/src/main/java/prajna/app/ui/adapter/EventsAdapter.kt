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
import prajna.app.repository.model.EventsInfo
import prajna.app.repository.model.InfoDetails
import prajna.app.repository.model.StudentFeeHistoryInfo
import prajna.app.utills.customFonts.Text_Normal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter(var context: Context, var dataList: ArrayList<EventsInfo>) :
    RecyclerView.Adapter<EventsAdapter.ViewHolder>() {


    private var mTokenData: String=""

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvEventName: Text_Normal = view.findViewById(R.id.tvEventName)
        var tvStartDate: Text_Normal = view.findViewById(R.id.tvStartDate)
        var tvEndDate: Text_Normal = view.findViewById(R.id.tvEndDate)
        var tvOrganiserName: Text_Normal = view.findViewById(R.id.tvOrganiserName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.events_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
//        mTokenData = UserSession(context).getStudentKey()


        holder.tvEventName.setText("Event : "+data.EventName)
        holder.tvOrganiserName.setText("Organiser : "+data.OrganiserName)

//        holder.tvAssignment_year.setText(data.YearCode)
        val startdate = data.StartDate
        val endadate = data.EndDate

        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val dt1 = format1.parse(startdate)
        val dt2 = format1.parse(endadate)


        val from_month: DateFormat = SimpleDateFormat("MMM")
        val from_year: DateFormat = SimpleDateFormat("yyyy")
        val from_day: DateFormat = SimpleDateFormat("dd")

        val mMonth: String = from_month.format(dt1)
        val mYear: String = from_year.format(dt1)
        val mDay: String = from_day.format(dt1)

        val mMonth1: String = from_month.format(dt2)
        val mYear1: String = from_year.format(dt2)
        val mDay1: String = from_day.format(dt2)

        holder.tvStartDate.setText("Start Date : "+mMonth + " " + mDay + ", " + mYear)
        holder.tvEndDate.setText("End Date : "+mMonth1 + " " + mDay1 + ", " + mYear1)



    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}