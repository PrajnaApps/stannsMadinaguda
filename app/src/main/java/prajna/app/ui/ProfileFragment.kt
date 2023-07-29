package prajna.app.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import prajna.app.R
import prajna.app.login.LoginActivity
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.StudentInfo
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.adapter.SelectStudentAdapter
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private var mProfilePic: String=""
    private var mUsername: String=""
    private lateinit var tvProEmail: Text_Medium
    private lateinit var tvProRoll: Text_Medium
    private lateinit var tvProName: Text_Medium
    private lateinit var tvProAdmissionDate: Text_Medium
    private lateinit var tvMarks: Text_Medium
    private lateinit var tvResPhone: Text_Medium
    private lateinit var ivProfilepic: CircleImageView
    private lateinit var tvLogout: Text_Bold

    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var rv_selectdhild: RecyclerView
    private val studentList: ArrayList<StudentInfo> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        mUsername= UserSession(requireActivity()).getLoginSave()

        initView(rootView)


//        val dict_json = JSONObject()
//        val jsonParser = JsonParser()
//        dict_json.put("ParentMobileNumber", mUsername)
//        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
//        Log.e("TAG", "exper_json: " + gsonObject)
//        if (isAdded && activity != null) {
////            getProfileData(gsonObject)
//
//        }

        return rootView

    }

    private fun initView(v: View) {
        tvHeadertitle = v.findViewById(R.id.tvHeadertitle)
        tvHeadertitle.setText(R.string.menu_profile)

        tvProName = v.findViewById(R.id.tvProName)
        tvProEmail = v.findViewById(R.id.tvProEmail)
        tvProRoll = v.findViewById(R.id.tvProRoll)
        tvProAdmissionDate = v.findViewById(R.id.tvAdmission)
        tvResPhone = v.findViewById(R.id.tvResPhone)
        tvMarks = v.findViewById(R.id.tvMarks)
        ivProfilepic = v.findViewById(R.id.ivProfilepic)
        tvLogout = v.findViewById(R.id.tvLogout)
        tvLogout.visibility=View.VISIBLE
        tvLogout.setOnClickListener(View.OnClickListener {
            UserSession(requireActivity()).removeLoginSave()
            UserSession(requireActivity()).removeStudentKey()
            UserSession(requireActivity()).removeDeviceId()
            UserSession(requireActivity()).remomveSelectPos()

            val intent = Intent(requireActivity(), LoginActivity::class.java)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()

        })


        rv_selectdhild = v.findViewById(R.id.rv_selectdhild)
        val layoutmanager = LinearLayoutManager(requireActivity())
        rv_selectdhild.layoutManager = layoutmanager
        rv_selectdhild.isNestedScrollingEnabled = false

        val dict_json = JSONObject()
        val jsonParser = JsonParser()
        dict_json.put("ParentMobileNumber", mUsername)
        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
        Log.e("TAG", "exper_json: " + gsonObject)

        getUserProfile(gsonObject)
    }

    private fun getUserProfile(hallticket: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(requireActivity()).getRetrofit()!!.create()
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
                        val stud_classname = jsonobj.getString("ClassName")
                        val stud_yearcode = jsonobj.getString("YearCode").toInt()
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
                        SelectStudentAdapter(requireActivity(), studentList,1)
                    rv_selectdhild.adapter = assignmentAdapter



                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}
