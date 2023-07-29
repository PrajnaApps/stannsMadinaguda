package prajna.app.ui.HomePageDetails

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.ResponseBody
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.Homescreen
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import prajna.app.utills.customFonts.Text_Normal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.text.DateFormat
import java.text.SimpleDateFormat

class AttendenceActivity : AppCompatActivity() {

    private var mPresent: Float=0f
    private var mAbsent: Float=0f
    private var mStudentKey: String=""
    private var mStud_name: String=""
    private var mStud_gender: String=""
    private var mTokenData: String=""
    private var mStud_DOB: String=""
    private var mYearCode: String=""
    lateinit var pieChart: PieChart
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold

    private lateinit var tvPresent_value: Text_Medium
    private lateinit var tvAbsent_value: Text_Medium

//    private lateinit var tvSubject: Text_Normal
    private lateinit var tvDayworking: Text_Normal
    private lateinit var tvDaypresent: Text_Normal
    private lateinit var tvName: Text_Normal
    private lateinit var tvGender: Text_Normal
    private lateinit var tvClass: Text_Normal
    private lateinit var tvStudDOB: Text_Normal

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendence_layout)

        mTokenData = UserSession(this@AttendenceActivity).getStudentKey()

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")
            mStud_name = dict_filterdata.getString("stud_name")
            mStud_gender = dict_filterdata.getString("stud_gender")
            mStud_DOB = dict_filterdata.getString("stud_dob")
            mYearCode = dict_filterdata.getString("stud_yearcode")


        } catch (e: Exception) {
            e.printStackTrace()
        }


        initialise()

        // on below line we are setting user percent value,
        // setting description as enabled and offset for pie chart
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(false)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.setDragDecelerationFrictionCoef(0.95f)

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        pieChart.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(12f)


    }

    private fun initialise() {
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)
        tvAbsent_value = findViewById(R.id.tvAbsent_value)
        tvPresent_value = findViewById(R.id.tvPresent_value)

//        tvSubject = findViewById(R.id.tvSubject)
        tvDaypresent = findViewById(R.id.tvDaypresent)
        tvDayworking = findViewById(R.id.tvDayworking)
        tvName = findViewById(R.id.tvName)
        tvGender = findViewById(R.id.tvGender)
        tvClass = findViewById(R.id.tvClass)
        tvStudDOB = findViewById(R.id.tvStudDOB)

        ivBack.visibility = View.VISIBLE
        tvHeadertitle.text = "Attendence"
        pieChart = findViewById(R.id.pieChart)


        tvName.setText(mStud_name)
        tvGender.setText(mStud_gender)

        val from_date = mStud_DOB

        val format1 = SimpleDateFormat("yyyy-MM-dd")
        val dt1 = format1.parse(from_date)


        val from_month: DateFormat = SimpleDateFormat("MMM")
        val from_year: DateFormat = SimpleDateFormat("yyyy")
        val from_day: DateFormat = SimpleDateFormat("dd")
        val mMonth: String = from_month.format(dt1)
        val mYear: String = from_year.format(dt1)
        val mDay: String = from_day.format(dt1)
        tvStudDOB.setText(mMonth + " " + mDay + ", " + mYear)



        llback.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        val dict_json = JSONObject()
        val jsonParser = JsonParser()
        dict_json.put("StudentPKey", mStudentKey)
        dict_json.put("YearCode", mYearCode)
        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
        Log.e("TAG", "exper_json: " + gsonObject)

        getAttendence(gsonObject)


    }

    private fun getAttendence(hallticket: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@AttendenceActivity).getRetrofit()!!.create()
        retrofitService.getAttendance(hallticket).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    val string = response.body()!!.string()
                    Log.e("TAG", "hallticket**** : " + string)


                    val jsonObject = JSONObject(string)

//                    tvSubject.setText(jsonObject.getString("SemesterSubjectName"))

//                     mPresent=jsonObject.getString("PresentPercentage").toFloat()
//                     mAbsent=jsonObject.getString("AbsentPercentage").toFloat()



                    val mTotaldays=jsonObject.getString("TotalWorkkingDays").toDouble()
                    val mPresentdays=jsonObject.getString("NoOfPresentDays").toDouble()

                    tvDayworking.setText(jsonObject.getString("TotalWorkkingDays"))
                    tvDaypresent.setText(jsonObject.getString("NoOfPresentDays"))
                    val mPercentage=mPresentdays/mTotaldays*100


                    Log.e("TAG", "Percentage*** : "+String.format("%.0f", mPercentage) )
                    val mFinalPresent=String.format("%.0f", mPercentage).toInt()

                    val FinalAbsent=100-mFinalPresent
                    Log.e("TAG", "absent*** : "+FinalAbsent)

                    mPresent= mFinalPresent.toFloat()
                    mAbsent= FinalAbsent.toFloat()




                    tvAbsent_value.setText("Presence % \n"+mPresent)
                    tvPresent_value.setText("Absence % \n"+mAbsent)

//                    if(jsonObject.getString("AbsentPercentage").equals(0)){
//                        mAbsent=100f
//                    }

                    /*Pie Chart Data*/
                    // on below line we are creating array list and
                    // adding data to it to display in pie chart
                    val entries: ArrayList<PieEntry> = ArrayList()
                    entries.add(PieEntry(mPresent))
                    entries.add(PieEntry(mAbsent))

                    // on below line we are setting pie data set
                    val dataSet = PieDataSet(entries, "Mobile OS")

                    // on below line we are setting icons.
                    dataSet.setDrawIcons(false)

                    // on below line we are setting slice for pie
                    dataSet.sliceSpace = 3f
                    dataSet.iconsOffset = MPPointF(0f, 40f)
                    dataSet.selectionShift = 5f

                    // add a lot of colors to list
                    val colors: ArrayList<Int> = ArrayList()
                    colors.add(resources.getColor(R.color.green))
                    colors.add(resources.getColor(R.color.red))

                    // on below line we are setting colors.
                    dataSet.colors = colors

                    // on below line we are setting pie data set
                    val data = PieData(dataSet)
                    data.setValueFormatter(PercentFormatter())
                    data.setValueTextSize(10f)
                    data.setValueTypeface(Typeface.DEFAULT_BOLD)
                    data.setValueTextColor(Color.WHITE)
                    pieChart.setData(data)

                    // undo all highlights
                    pieChart.highlightValues(null)

                    // loading chart
                    pieChart.invalidate()

                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@AttendenceActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AttendenceActivity, Homescreen::class.java))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }

}