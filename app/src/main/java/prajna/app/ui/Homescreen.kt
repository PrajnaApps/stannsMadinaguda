package prajna.app.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.HomePageDetails.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import prajna.app.ui.HomePageDetails.GalleryActivity

class Homescreen : AppCompatActivity() {

    private var mStudentKey: String=""
    private var mTokenData: String=""
    private var mTokenID: String=""
    private var mUsername: String=""
    private var mNotify: String=""
    private var mId: String=""
    private var mScreen: String=""
    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homescreen)




        mId=intent.getStringExtra("id").toString()
        bottomNav = findViewById(R.id.bottomNav)

        mNotify= UserSession(this@Homescreen).getNotification()
        mUsername= UserSession(this@Homescreen).getLoginSave()
        mTokenData = UserSession(this@Homescreen).getStudentKey()
        mTokenID=   UserSession(this@Homescreen).getDeviceId()
        Log.e("TAG", "devide_id*** : "+mTokenID )

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")
        } catch (e: Exception) {
            e.printStackTrace()
        }


        val dict_json = JSONObject()
        val jsonParser = JsonParser()
        dict_json.put("ParentMobileNumber", mUsername)
        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
        Log.e("TAG", "exper_json: " + gsonObject)
//        getProfileData(gsonObject)

        if(mId.equals("history")){
            loadFragment(FeeURLFragment())
            bottomNav.setSelectedItemId(R.id.second_fragment);

        }else if(mId.equals("profile")){
            loadFragment(ProfileFragment())
            bottomNav.setSelectedItemId(R.id.fourth_fragment);

        }else{
            loadFragment(HomeFragment())
        }
        Log.e("TAG", "onCreate: "+mNotify )
        if(mNotify.equals("11")){
            loadFragment(NotifyFragment())
            bottomNav.setSelectedItemId(R.id.third_fragment);

        }

//        FirebaseMessaging.getInstance().subscribeToTopic("prajna_msg")
//            .addOnSuccessListener {
//
//            }.addOnFailureListener {
//                Toast.makeText(
//                    applicationContext,
//                    it.localizedMessage,
//                    Toast.LENGTH_LONG
//                ).show()
//            }


        val notify_json = JSONObject()
        notify_json.put("TokenId", "")
        notify_json.put("Token", mTokenID)
        notify_json.put("StudentFKey", mStudentKey)
        notify_json.put("CreatedBy", 1)
        val notifyObject = jsonParser.parse(notify_json.toString()) as JsonObject
        Log.e("TAG", "notify_json: " + notifyObject)

        getNotification(notifyObject)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.first_fragment -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.second_fragment -> {
                    loadFragment(FeeURLFragment())
                    true
                }
                R.id.third_fragment -> {
                    loadFragment(NotifyFragment())
                    true
                }
                R.id.fourth_fragment -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> throw AssertionError()

            }
        }
//        val fcm: MyFire = FirebaseMessaging.getInstance()
//
//        // Build the message to be sent
//
//        // Build the message to be sent
//        val message: RemoteMessage = Builder("your_sender_id")
//            .setMessageId("unique_message_id")
//            .addData("title", "Hello World!")
//            .addData("body", "This is a test notification.")
//            .build()

        // Send the message

        // Send the message
//        fcm.send(message)
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun getProfileData(gsonObject: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@Homescreen).getRetrofit()!!.create()
        retrofitService.getProfile(gsonObject).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                val string = response.body()!!.string()
                Log.e("TAG", "onResponse: " + string)

//                Log.e("TAG", "onResponse() called with: call = [" + call.request().url + "], response = [" + string + "]");

                if(!string.equals("")){
                    val jsonarray = JSONArray(string)
                    val dataobj = jsonarray.getJSONObject(0)

                    val mStuKey=dataobj.getString("StudentPKey")

                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Homescreen, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
        fun opendetailPage(page:Int){

        if(page==0){
            startActivity(Intent(this@Homescreen, AttendenceActivity::class.java))
            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
            finish()
        }else if(page==1){
            startActivity(Intent(this@Homescreen, EventsActivity::class.java))
            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
            finish()
        }else if(page==2){
            startActivity(Intent(this@Homescreen, GalleryActivity::class.java))
            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
            finish()
        }else if(page==3){
            startActivity(Intent(this@Homescreen, ProgressReport_Activity::class.java))
            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
            finish()
        }else if(page==4){
            startActivity(Intent(this@Homescreen, AssignmentActivity::class.java))
            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
            finish()
        }else if(page==5){
            startActivity(Intent(this@Homescreen, LearningsActivity::class.java))
            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
            finish()
        }else if(page==6){
            bottomNav = findViewById(R.id.bottomNav)
            bottomNav.setSelectedItemId(R.id.second_fragment);


        }else if(page==7){
            startActivity(Intent(this@Homescreen, FeedbackActivity::class.java))
            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
            finish()
        }else if(page==8){
            startActivity(Intent(this@Homescreen, QueryActivity::class.java))
            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
            finish()
        }else if(page==9){
            startActivity(Intent(this@Homescreen, HelpActivity::class.java))
            overridePendingTransition(R.anim.move_left_enter, R.anim.move_left_exit)
            finish()
        }

    }
    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.second_fragment,fragment)
            commit()
        }


    private fun getNotification(notify: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@Homescreen).getRetrofit()!!.create()
        retrofitService.getNotificationFirebase(notify).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    val string = response.body()!!.string()
                    Log.e("TAG", "notify**** : " + string)

                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Homescreen, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }




}