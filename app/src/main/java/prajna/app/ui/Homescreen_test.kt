//package prajna.app.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.FrameLayout
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import com.google.gson.JsonObject
//import com.google.gson.JsonParser
//import com.qlx.myviejas.utills.sharedPreferences.UserSession
//import me.ibrahimsn.lib.OnItemSelectedListener
//import me.ibrahimsn.lib.SmoothBottomBar
//import okhttp3.ResponseBody
//import org.json.JSONArray
//import org.json.JSONObject
//import prajna.app.R
//import prajna.app.repository.api.ApiServices
//import prajna.app.repository.retrofitservice.ServiceBuilder
//import prajna.app.ui.HomePageDetails.*
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.create
//
//class Homescreen : AppCompatActivity() {
//
//    private lateinit var bottomBar: SmoothBottomBar
//    private lateinit var frame: FrameLayout
//    private var mUsername: String=""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.homescreen)
//
//        mUsername= UserSession(this@Homescreen).getLoginSave()
//
//        val dict_json = JSONObject()
//        val jsonParser = JsonParser()
//        dict_json.put("ParentMobileNumber", mUsername)
//        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
//        Log.e("TAG", "exper_json: " + gsonObject)
//        getProfileData(gsonObject)
//
//        initView()
//        setupBottomBar()
//    }
//
//    private fun initView() {
//        frame = findViewById(R.id.frame)
//        bottomBar = findViewById(R.id.bottomBar)
//    }
//    private fun setupBottomBar() {
//        replace(HomeFragment())
//        bottomBar.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelect(i: Int): Boolean {
//                when (i) {
//                    0 -> replace(HomeFragment())
//                    1 -> replace(FeeFragment())
//                    2 -> replace(NotifyFragment())
//                    3 -> replace(ProfileFragment())
//                }
//                return true
//            }
//        }
//    }
//
//    fun replace(_fragment: Fragment?) {
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.frame, _fragment!!)
//        transaction.commit()
//    }
//    private fun getProfileData(gsonObject: JsonObject) {
//
////        pbar.visibility = View.VISIBLE
//        val retrofitService: ApiServices =
//            ServiceBuilder(this@Homescreen).getRetrofit()!!.create()
//        retrofitService.getProfile(gsonObject).enqueue(object : Callback<ResponseBody> {
//
//            override fun onResponse(
//                call: Call<ResponseBody>, response: Response<ResponseBody>
//            ) {
//                val string = response.body()!!.string()
//                Log.e("TAG", "onResponse: " + string)
//
////                Log.e("TAG", "onResponse() called with: call = [" + call.request().url + "], response = [" + string + "]");
//
//                if(!string.equals("")){
//                    val jsonarray = JSONArray(string)
//                    val dataobj = jsonarray.getJSONObject(0)
//
//                    val mStuKey=dataobj.getString("StudentPKey")
//
//                }
//
//
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Toast.makeText(this@Homescreen, t.message, Toast.LENGTH_LONG).show()
//            }
//        })
//    }
//
//        fun opendetailPage(page:Int){
//
//        if(page==0){
//            startActivity(Intent(this@Homescreen, AttendenceActivity::class.java))
//            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
//            finish()
//        }else if(page==1){
//            startActivity(Intent(this@Homescreen, EventsActivity::class.java))
//            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
//            finish()
//        }else if(page==2){
//            startActivity(Intent(this@Homescreen, GalleryActivity::class.java))
//            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
//            finish()
//        }else if(page==3){
//            startActivity(Intent(this@Homescreen, ProgressCardActivity::class.java))
//            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
//            finish()
//        }else if(page==4){
//            startActivity(Intent(this@Homescreen, AssignmentActivity::class.java))
//            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
//            finish()
//        }else if(page==5){
//            startActivity(Intent(this@Homescreen, LearningsActivity::class.java))
//            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
//            finish()
//        }else if(page==6){
//            startActivity(Intent(this@Homescreen, FeeActivity::class.java))
//            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
//            finish()
//        }else if(page==7){
//            startActivity(Intent(this@Homescreen, FeedbackActivity::class.java))
//            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
//            finish()
//        }else if(page==8){
//            startActivity(Intent(this@Homescreen, QueryActivity::class.java))
//            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
//            finish()
//        }
//
//    }
//
//
//}