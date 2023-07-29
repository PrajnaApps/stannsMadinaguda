package prajna.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import prajna.app.R
import prajna.app.repository.model.Progressreport
import prajna.app.ui.HomePageDetails.ProgressCardActivity
import prajna.app.ui.Homescreen
import prajna.app.utills.customFonts.Text_Normal
import java.util.*

class ProgressReportAdapter(var context: Context, var dataList: ArrayList<Progressreport>) :
    RecyclerView.Adapter<ProgressReportAdapter.ViewHolder>() {


    private var mTokenData: String=""

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvUnitTest: Text_Normal = view.findViewById(R.id.tvUnitTest)
        var cardview: CardView = view.findViewById(R.id.cardview)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.progressreport_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
//        mTokenData = UserSession(context).getStudentKey()


        holder.tvUnitTest.setText(data.ExamName)
        holder.cardview.setOnClickListener(View.OnClickListener {
            val data = dataList[position]

            context.startActivity(Intent(context, ProgressCardActivity::class.java).putExtra("subjects", data.Subjects).putExtra("unit_test", data.ExamName))



        })


    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}