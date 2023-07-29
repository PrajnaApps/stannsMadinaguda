package prajna.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.model.StudentInfo
import prajna.app.ui.Homescreen
import prajna.app.utills.customFonts.Text_Medium
import prajna.app.utills.customFonts.Text_Normal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class SelectStudentAdapter(var context: Context, var dataList: ArrayList<StudentInfo>, var type:Int) :
    RecyclerView.Adapter<SelectStudentAdapter.ViewHolder>() {

    private var mPos: Int=0
    private var mSelect: Int=-1
    var selectedPosition = -1


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvStudName: Text_Medium = view.findViewById(R.id.tvStudName)
        var tvStudDOB: Text_Normal = view.findViewById(R.id.tvStudDOB)
        var tvStudGender: Text_Normal = view.findViewById(R.id.tvStudGender)
        var llselectstudent: LinearLayout = view.findViewById(R.id.llSelectstudent)
        var tvStudAadharṣ: Text_Normal = view.findViewById(R.id.tvStudAadharṣ)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.selectchild_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        mSelect = UserSession(context).getSelectPos()

        if(mSelect==position){
            holder.llselectstudent.setBackgroundColor(context.getColor(R.color.divider))
        }

//        if(mTokenData.length>0){
//            var dict_filterdata = JSONObject()
//            try {
//                dict_filterdata = JSONObject(mTokenData)
//                mPos = dict_filterdata.getString("position").toInt()
//                Log.e("TAG", "position** : "+mPos )
//                if(position==mPos) {
//                    holder.radiobutton.setBackgroundResource(R.drawable.indicator_selected)
//                }
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }




        holder.tvStudAadharṣ.setText("Class : "+data.ClassName)
        holder.tvStudName.setText("Name : "+data.StudentName)
        holder.tvStudGender.setText("Gender : "+data.StudentGender)
//        holder.tvStudAadharṣ.setText(data.AadharNo).toString()

//        holder.tvAssignment_year.setText(data.YearCode)
        val from_date = data.DateOfBirth

        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val dt1 = format1.parse(from_date)


        val from_month: DateFormat = SimpleDateFormat("MMM")
        val from_year: DateFormat = SimpleDateFormat("yyyy")
        val from_day: DateFormat = SimpleDateFormat("dd")
        val mMonth: String = from_month.format(dt1)
        val mYear: String = from_year.format(dt1)
        val mDay: String = from_day.format(dt1)
        holder.tvStudDOB.setText("Date of Birth : "+mMonth + " " + mDay + ", " + mYear)

//        holder.radiobutton.isChecked = position == selectedPosition
        val token_json = JSONObject()

//        holder.radiobutton.setOnClickListener {
////            selectedPosition = position
////            notifyDataSetChanged()
////
////            token_json.put("stud_classcode", data.ClassCode.toString())
////            token_json.put("stud_yearcode", data.YearCode.toString())
//////            token_json.put("stud_sectioncode", data.SectionCode.toString())
////            token_json.put("stud_key", data.StudentPKey.toString())
////            token_json.put("stud_admissionNo", data.AdmissionNo.toString())
////            token_json.put("stud_father", data.FatherName.toString())
////            token_json.put("position", position)
////            token_json.put("stud_name", data.StudentName)
////            token_json.put("stud_gender", data.StudentGender)
////            token_json.put("stud_dob", data.DateOfBirth)
////            Log.e("TAG", "decodeToken: " + token_json)
////            UserSession(context).setStudentKey(token_json.toString())
////            context.startActivity(Intent(context, Homescreen::class.java))
//
////            if(type==1){
////            }
//
//        }



        holder.llselectstudent.setOnClickListener(View.OnClickListener {
            holder.llselectstudent.setBackgroundColor(context.getColor(R.color.white))

            selectedPosition = position
            notifyDataSetChanged()
            UserSession(context).setSelectPos(position)

            token_json.put("stud_classcode", data.ClassCode.toString())
            token_json.put("stud_yearcode", data.YearCode.toString())
//            token_json.put("stud_sectioncode", data.SectionCode.toString())
            token_json.put("stud_key", data.StudentPKey.toString())
            token_json.put("stud_admissionNo", data.AdmissionNo.toString())
            token_json.put("stud_father", data.FatherName.toString())
            token_json.put("position", position)
            token_json.put("stud_name", data.StudentName)
            token_json.put("stud_gender", data.StudentGender)
            token_json.put("stud_dob", data.DateOfBirth)
            Log.e("TAG", "decodeToken: " + token_json)
            UserSession(context).setStudentKey(token_json.toString())
            context.startActivity(Intent(context, Homescreen::class.java))
        })


    }


    override fun getItemCount(): Int {
        return dataList.size
    }


}