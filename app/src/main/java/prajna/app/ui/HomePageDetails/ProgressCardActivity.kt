package prajna.app.ui.HomePageDetails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.json.JSONArray
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.ProgressreportInfo
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.adapter.UnitTestReportAdapter
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create


class ProgressCardActivity : AppCompatActivity() {

    private lateinit var labels: ArrayList<String>

    private var mSubjectList: String = ""
    private var mUnitTest: String = ""
    private var mMarksval: Float = 0.0f
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
//    var entries = ArrayList<BarEntry>()

    private lateinit var rvUnitResult: RecyclerView
    private lateinit var tvEmpty: Text_Medium



    private val subjectList: ArrayList<ProgressreportInfo> = ArrayList()

    lateinit var barChart: BarChart

    // on below line we are creating
    // a variable for bar data

    // on below line we are creating a
    // variable for bar data set
    lateinit var barDataSet: BarDataSet

    // on below line we are creating array list for bar data
    lateinit var barEntriesArrayList: ArrayList<BarEntry>
    var colors = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.progresscard)


        mSubjectList=intent.getStringExtra("subjects").toString().trim()
        mUnitTest=intent.getStringExtra("unit_test").toString().trim()
//        Log.e("TAG", "subjest_list: "+mSubjectList )



        initialise()


    }

    private fun initialise() {
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)
        rvUnitResult = findViewById(R.id.rvUnitResult)
        tvEmpty = findViewById(R.id.tvEmpty)
        tvHeadertitle.setText(mUnitTest)
        ivBack.visibility = View.VISIBLE



        val layoutmanager = LinearLayoutManager(this@ProgressCardActivity)
        rvUnitResult.layoutManager = layoutmanager
        rvUnitResult.isNestedScrollingEnabled = false
        barChart = findViewById(R.id.idBarChart);

        barChart.setScaleEnabled(false);

        barEntriesArrayList = ArrayList()

        colors.add(ContextCompat.getColor(this, R.color.rating_color));
        colors.add(ContextCompat.getColor(this, R.color.blue));
        colors.add(ContextCompat.getColor(this, R.color.teal_200));
        colors.add(ContextCompat.getColor(this, R.color.red));

        llback.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        /*RecyclerView*/
        val jsonar = JSONArray(mSubjectList)
//        Log.e("TAG", "subjest_list: "+jsonar )

        if(jsonar.length()>0){
            for (i in 0 until jsonar.length()){
                val jsonobj = jsonar.getJSONObject(i)
                val Marks = jsonobj.getString("Marks")
                val Subjects = jsonobj.getString("SubjectName")

                subjectList.add(ProgressreportInfo(Subjects,Marks))

            }
            val assignmentAdapter = UnitTestReportAdapter(this@ProgressCardActivity, subjectList)
            rvUnitResult.adapter = assignmentAdapter
            rvUnitResult.visibility=View.VISIBLE
            tvEmpty.visibility=View.GONE
        }else{
            rvUnitResult.visibility=View.GONE
            tvEmpty.visibility=View.VISIBLE
        }

        /**/


        Log.e("TAG", "    2210: " + jsonar.length())
        labels = ArrayList<String>()

        if (jsonar.length() > 0) {
            for (j in 0 until jsonar.length()) {
                val jsonobj = jsonar.getJSONObject(j)
                val SubjectName = jsonobj.getString("SubjectName")
                val Marks = jsonobj.getString("Marks")


                labels.add(SubjectName)
                mMarksval=Marks.toFloat()

                barEntriesArrayList.add(BarEntry(j.toFloat(),mMarksval))
                barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)




                val set = BarDataSet(barEntriesArrayList, "Subjects")
                set.setColors(colors);

                set.valueTextSize = 17f

                barChart.data = BarData(set)
                barChart.invalidate()
            }
            barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

            barChart.setDrawGridBackground(false)
//                        barChart.axisLeft.isEnabled = false
            barChart.axisRight.isEnabled = false
            barChart.description.isEnabled = false
            barChart.getAxisLeft().setDrawGridLines(false);
            barChart.getXAxis().setDrawGridLines(false);
            barChart.axisLeft.axisMinimum = 0f








        }



    }

    private fun getProgressCard(hallticket: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@ProgressCardActivity).getRetrofit()!!.create()
        retrofitService.getPrgressCard(hallticket).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    val string = response.body()!!.string()
                    Log.e("TAG", "hallticket**** : " + string)



                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@ProgressCardActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@ProgressCardActivity, ProgressReport_Activity::class.java))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }

}