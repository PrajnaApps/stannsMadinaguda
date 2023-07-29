package prajna.app.ui

import android.app.ProgressDialog
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
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.NotificationInfo
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.adapter.NotificationAdapter
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create


/**
 * A simple [Fragment] subclass.
 */
class NotifyFragment : Fragment() {

    private var mStudentKey: String = ""
    private var mTokenData: String = ""
    private var mUsername: String = ""
    private lateinit var tvProEmail: Text_Medium
    private lateinit var tvProRoll: Text_Medium
    private lateinit var tvProName: Text_Medium
    private lateinit var tvProAdmissionDate: Text_Medium
    private lateinit var tvMarks: Text_Medium
    private lateinit var tvEmpty: Text_Medium
    private lateinit var tvResPhone: Text_Medium
//    private lateinit var ivProfilepic: CircleImageView
    private lateinit var tvLogout: Text_Bold

    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var rv_selectdhild: RecyclerView
    private val studentList: ArrayList<NotificationInfo> = ArrayList()
    private lateinit var mProgressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_notify, container, false)

        mUsername = UserSession(requireActivity()).getLoginSave()
        mTokenData = UserSession(requireActivity()).getStudentKey()

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")

        } catch (e: Exception) {
            e.printStackTrace()
        }
        mProgressDialog = ProgressDialog(requireActivity())
        mProgressDialog.isIndeterminate = true

        UserSession(requireActivity()).removeNotification()

        initView(rootView)


//        val dict_json = JSONObject()
//        val jsonParser = JsonParser()
//        dict_json.put("ParentMobileNumber", mUsername)
//        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
//        Log.e("TAG", "exper_json: " + gsonObject)

        return rootView

    }

    private fun initView(v: View) {
        tvHeadertitle = v.findViewById(R.id.tvHeadertitle)
        tvHeadertitle.setText(R.string.notification)

        tvProName = v.findViewById(R.id.tvProName)
        tvProEmail = v.findViewById(R.id.tvProEmail)
        tvProRoll = v.findViewById(R.id.tvProRoll)
        tvProAdmissionDate = v.findViewById(R.id.tvAdmission)
        tvResPhone = v.findViewById(R.id.tvResPhone)
        tvMarks = v.findViewById(R.id.tvMarks)
        tvEmpty = v.findViewById(R.id.tvEmpty)

        tvLogout = v.findViewById(R.id.tvLogout)
        tvLogout.visibility = View.GONE


        rv_selectdhild = v.findViewById(R.id.rv_selectdhild)
        val layoutmanager = LinearLayoutManager(requireActivity())
        rv_selectdhild.layoutManager = layoutmanager
        rv_selectdhild.isNestedScrollingEnabled = false

        val dict_json = JSONObject()
        val jsonParser = JsonParser()
        dict_json.put("StudentPKey", mStudentKey)
        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
        Log.e("TAG", "exper_json: " + gsonObject)
        if (isAdded && activity != null) {
            getNotificationList(gsonObject)

        }

    }

    private fun getNotificationList(hallticket: JsonObject) {

        mProgressDialog.setMessage("Loading...")
        mProgressDialog.show()
        val retrofitService: ApiServices =
            ServiceBuilder(requireActivity()).getRetrofit()!!.create()
        retrofitService.getNotificationList(hallticket).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    val string = response.body()!!.string()
                    Log.e("TAG", "hallticket**** : " + string)

                    val jsonar = JSONArray(string)
                    if(jsonar.length()>0){
                        for (i in 0 until jsonar.length()) {
                            val jsonobj = jsonar.getJSONObject(i)
                            val AlertMessage = jsonobj.getString("AlertMessage")


                            studentList.add(NotificationInfo(AlertMessage))

                        }
                        val assignmentAdapter = NotificationAdapter(requireActivity(), studentList)
                        rv_selectdhild.adapter = assignmentAdapter
                        rv_selectdhild.visibility=View.VISIBLE
                        tvEmpty.visibility=View.GONE
                        mProgressDialog.dismiss()

                    }else{
                        rv_selectdhild.visibility=View.GONE
                        tvEmpty.visibility=View.VISIBLE
                        mProgressDialog.dismiss()}

                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}
