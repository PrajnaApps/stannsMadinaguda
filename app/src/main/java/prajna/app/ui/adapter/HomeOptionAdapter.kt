package prajna.app.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.json.JSONObject
import prajna.app.R
import prajna.app.ui.Homescreen
import prajna.app.utills.customFonts.Text_Medium
import java.util.*

class HomeOptionAdapter(var context: Context) :
    RecyclerView.Adapter<HomeOptionAdapter.ViewHolder>() {

        private val homescree_icon = arrayOf(R.drawable.attendance,R.drawable.events,R.drawable.gallery,
        R.drawable.progress_report,R.drawable.ic_assignment,R.drawable.learning,R.drawable.fees,R.drawable.feedback,R.drawable.queries,
            R.drawable.help_d)
    private val names = arrayOf("Attendence","Events","Gallery","Progress Card","Assignments","Learnings",
        "Fee","Feedback","Queries","Help")


    var selectedItemPos = -1
    var lastItemSelectedPos = -1
    val dict_json = JSONObject()
    var mCityId: String = ""

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_city: ImageView = view.findViewById(R.id.iv_city)
        var tvcityname: Text_Medium = view.findViewById(R.id.tvcityname)
        var llhome: LinearLayout = view.findViewById(R.id.llhome)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cityselect_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val data = cityList[position]
//        val cId = cityList[position]._id
        Glide.with(context).load(homescree_icon[position]).into(holder.iv_city)

        holder.tvcityname.text = names[position]

        holder.llhome.setOnClickListener(View.OnClickListener {
            val name=names[position]
            (context as Homescreen).opendetailPage(position)
        })



    }

    override fun getItemCount(): Int {
        return homescree_icon.size
    }


}