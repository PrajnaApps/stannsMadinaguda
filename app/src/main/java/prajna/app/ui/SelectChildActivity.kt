package prajna.app.ui

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.StudentInfo
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.adapter.SelectStudentAdapter
import prajna.app.utills.customFonts.Button_Bold
import prajna.app.utills.customFonts.Text_Bold
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class SelectChildActivity : AppCompatActivity() {
    private var mTokenData: String=""
    private var mUserName: String=""
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var btSubmit: Button_Bold
    private lateinit var rv_selectdhild: RecyclerView
    private val studentList: ArrayList<StudentInfo> = ArrayList()
    private lateinit var mProgressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectchild_layout)
        mTokenData = UserSession(this@SelectChildActivity).getStudentKey()
        mUserName=UserSession(this@SelectChildActivity).getLoginSave()
        Log.e("TAG", "user********: "+mUserName )

        mProgressDialog = ProgressDialog(this)
        mProgressDialog.isIndeterminate = true

        initView()
    }

    private fun initView() {
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)
        btSubmit = findViewById(R.id.btSubmit)

        tvHeadertitle.setText("Select Child")
        ivBack.visibility = View.VISIBLE

        rv_selectdhild = findViewById(R.id.rv_selectdhild)
        val layoutmanager = LinearLayoutManager(this@SelectChildActivity)
        rv_selectdhild.layoutManager = layoutmanager
        rv_selectdhild.isNestedScrollingEnabled = false



        llback.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        val dict_json = JSONObject()
        val jsonParser = JsonParser()
        dict_json.put("ParentMobileNumber", mUserName)
        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
        Log.e("TAG", "exper_json: " + gsonObject)

        getUserProfile(gsonObject)

        btSubmit.setOnClickListener(View.OnClickListener {

            if(!mTokenData.equals("")){
                startActivity(Intent(this@SelectChildActivity, Homescreen::class.java))
                overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
                finish()
            }else{
                Toast.makeText(this@SelectChildActivity,"Select Child",Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getUserProfile(hallticket: JsonObject) {
        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()
//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@SelectChildActivity).getRetrofit()!!.create()
        retrofitService.getProfile(hallticket).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    val string = response.body()!!.string()
                    Log.e("TAG", "hallticket**** : " + string)

                    val jsonar = JSONArray(string)
                    for (i in 0 until jsonar.length()) {
                        val jsonobj = jsonar.getJSONObject(i)
                        val stud_name = jsonobj.getString("StudentName")
                        val stud_dob = jsonobj.getString("DateOfBirth")
                        val stud_aadhar = jsonobj.getInt("AadharNo")
                        val stud_fathername = jsonobj.getString("FatherName")
                        val stud_gen = jsonobj.getString("StudentGender")
                        val stud_admis = jsonobj.getString("AdmissionNo")
                        val stud_classcode = jsonobj.getString("ClassCode").toInt()
                        val stud_yearcode = jsonobj.getString("YearCode").toInt()
                        val stud_classname = jsonobj.getString("ClassName")

//                        val stud_sectioncode = jsonobj.getString("SectionCode").toInt()
                        val stud_key = jsonobj.getString("StudentPKey").toInt()

                        studentList.add(
                            StudentInfo(true,
                                stud_name,
                                stud_dob,
                                stud_aadhar,
                                stud_fathername,
                                stud_gen,
                                stud_admis,
                                stud_classcode,
                                stud_classname,
                                stud_yearcode,
                                stud_key
                            )
                        )


                    }
                    val assignmentAdapter =
                        SelectStudentAdapter(this@SelectChildActivity, studentList,0)
                    rv_selectdhild.adapter = assignmentAdapter
                    mProgressDialog.dismiss()

                }else{
                    mProgressDialog.dismiss()
                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SelectChildActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun selectedStudent(studentInfo: StudentInfo) {

//        Toast.makeText(this@SelectChildActivity,studentInfo.StudentPKey,Toast.LENGTH_SHORT).show()
    }


    override fun onBackPressed() {
        super.onBackPressed()
      finish()
    }
}