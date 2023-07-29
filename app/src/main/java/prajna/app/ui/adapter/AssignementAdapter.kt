package prajna.app.ui.adapter

import android.content.Context
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import org.json.JSONObject
import prajna.app.R
import prajna.app.listner.AssignmentListner
import prajna.app.repository.model.InfoDetails
import prajna.app.utills.customFonts.Text_Normal
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AssignementAdapter(var context: Context, var dataList: ArrayList<InfoDetails>,var assignmentListner: AssignmentListner) :
    RecyclerView.Adapter<AssignementAdapter.ViewHolder>() {


    private var mTokenData: String=""

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvAssignment_des: Text_Normal = view.findViewById(R.id.tvAssignment_des)
        var tvAssignment_date: Text_Normal = view.findViewById(R.id.tvAssignment_date)
        var tvName: Text_Normal = view.findViewById(R.id.tvName)
        var tvDOB: Text_Normal = view.findViewById(R.id.tvDOB)
        var llDownloadFile: LinearLayout = view.findViewById(R.id.llDownloadFile)
        var tvGender: Text_Normal = view.findViewById(R.id.tvGender)
//        var tvAssignment_year: Text_Normal = view.findViewById(R.id.tvAssignment_year)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.assignment_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        mTokenData = UserSession(context).getStudentKey()

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)

            holder.tvName.setText("Name : "+dict_filterdata.getString("stud_name"))
            holder.tvGender.setText("Gender : "+dict_filterdata.getString("stud_gender"))


        } catch (e: Exception) {
            e.printStackTrace()
        }
        holder.tvAssignment_des.setText(data.AssignmentDescription)

//        holder.tvAssignment_year.setText(data.YearCode)
        val from_date = dict_filterdata.getString("stud_dob")
        val assignment_date = data.AssignmentDate

        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val dt1 = format1.parse(from_date)
        val assign_dt1 = format1.parse(assignment_date)


        val from_month: DateFormat = SimpleDateFormat("MMM")
        val from_year: DateFormat = SimpleDateFormat("yyyy")
        val from_day: DateFormat = SimpleDateFormat("dd")

        val mMonth: String = from_month.format(dt1)
        val mYear: String = from_year.format(dt1)
        val mDay: String = from_day.format(dt1)

        val mMonth1: String = from_month.format(assign_dt1)
        val mYear1: String = from_year.format(assign_dt1)
        val mDay1: String = from_day.format(assign_dt1)

        holder.tvDOB.setText("DOB : "+mMonth + " " + mDay + ", " + mYear)
        holder.tvAssignment_date.setText(mMonth1 + " " + mDay1 + ", " + mYear1)


        holder.llDownloadFile.setOnClickListener(View.OnClickListener {
            if(!data.Path.equals("")){


                val destinationPath = "/path/to/destination/directory"
                assignmentListner.onDownloadPath(data.Path,data.FileName)

//                downloadPngFromUrl(data.Path, downloadFolder.toString())
            }
        })



    }



    override fun getItemCount(): Int {
        return dataList.size
    }

}