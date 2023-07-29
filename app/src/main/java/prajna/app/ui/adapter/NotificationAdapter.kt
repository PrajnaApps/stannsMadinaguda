package prajna.app.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import prajna.app.R
import prajna.app.repository.model.NotificationInfo
import prajna.app.repository.model.StudentFeeHistoryInfo
import prajna.app.utills.customFonts.Text_Normal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(var context: Context, var dataList: ArrayList<NotificationInfo>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    private var mTokenData: String=""

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvAlert: Text_Normal = view.findViewById(R.id.tvAlert)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
//        mTokenData = UserSession(context).getStudentKey()


        holder.tvAlert.setText(data.AlertMessage)





    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}