package prajna.app.ui.HomePageDetails

import android.Manifest
import android.app.DownloadManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import prajna.app.R
import prajna.app.listner.AssignmentListner
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.InfoDetails
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.Homescreen
import prajna.app.ui.adapter.AssignementAdapter
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class AssignmentActivity : AppCompatActivity(),AssignmentListner {

    private var mTokenData: String=""
    private var mStudentKey: String=""
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var rv_assignment: RecyclerView
    private lateinit var tvEmpty: Text_Medium
    private val assignmentList: ArrayList<InfoDetails> = ArrayList()

    private val REQUEST_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.assignment_layout)

        mTokenData = UserSession(this@AssignmentActivity).getStudentKey()

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")

        } catch (e: Exception) {
            e.printStackTrace()
        }


        initialise()

// storage runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0
                )

            }
        }

    }

    private fun initialise() {
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)
        rv_assignment = findViewById(R.id.rv_assignment)
        tvEmpty = findViewById(R.id.tvEmpty)
        tvHeadertitle.setText("Assignments")
        ivBack.visibility = View.VISIBLE



        val layoutmanager = LinearLayoutManager(this@AssignmentActivity)
        rv_assignment.layoutManager = layoutmanager
        rv_assignment.isNestedScrollingEnabled = false

        llback.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        val dict_json = JSONObject()
        val jsonParser = JsonParser()
        dict_json.put("StudentPKey", mStudentKey)
        val gsonObject = jsonParser.parse(dict_json.toString()) as JsonObject
        Log.e("TAG", "exper_json: " + gsonObject)

        getAssignment(gsonObject)


    }

    private fun getAssignment(hallticket: JsonObject) {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@AssignmentActivity).getRetrofit()!!.create()
        retrofitService.getAssignment(hallticket).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    val string = response.body()!!.string()
                    Log.e("TAG", "hallticket**** : " + string)

                    val jsonar=JSONArray(string)

                    if(jsonar.length()>0){
                        for (i in 0 until jsonar.length()){
                            val jsonobj = jsonar.getJSONObject(i)
                            val class_code=jsonobj.getString("ClassCode").toInt()
                            val ass_des=jsonobj.getString("AssignmentDescription")
                            val ass_date=jsonobj.getString("AssignmentDate")
                            val sec_code=jsonobj.getString("SectionCode").toInt()
                            val sub_code=jsonobj.getString("SubjectCode").toInt()
                            val school_id=jsonobj.getString("SchoolId").toInt()
                            val branch_id=jsonobj.getString("BranchId").toInt()
                            val year_code=jsonobj.getString("YearCode").toInt()
                            val isActive=jsonobj.getString("IsActive").toBoolean()
                            val filename=jsonobj.getString("FileName")
                            val path=jsonobj.getString("Path")

                            assignmentList.add(InfoDetails(class_code,ass_des,ass_date,sec_code,sub_code,school_id,branch_id,
                                year_code,isActive,filename,path))


                        }
                        val assignmentAdapter = AssignementAdapter(this@AssignmentActivity, assignmentList,this@AssignmentActivity)
                        rv_assignment.adapter = assignmentAdapter
                        rv_assignment.visibility=View.VISIBLE
                        tvEmpty.visibility=View.GONE
                    }else{
                        rv_assignment.visibility=View.GONE
                        tvEmpty.visibility=View.VISIBLE
                    }



                }else{
                    rv_assignment.visibility=View.GONE
                    tvEmpty.visibility=View.VISIBLE
                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@AssignmentActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun downloadPngFromUrl(urlString: String, destinationPath: String) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection()
            connection.connect()

            // Get the file name from the URL and create the destination file
            val fileName = url.path.substringAfterLast('/')
            val destinationFile = File(destinationPath, fileName)

            BufferedInputStream(connection.getInputStream()).use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        outputStream.write(buffer, 0, bytesRead)
                    }
                    outputStream.flush()
                }
            }
            Toast.makeText(this@AssignmentActivity,"Downloaded Successfully",Toast.LENGTH_SHORT).show()

//            println("Image downloaded successfully.")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AssignmentActivity, Homescreen::class.java))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }

    override fun onDownloadPath(downloadpath: String,filname: String) {

        downloadImage(downloadpath, filname);

    }

    fun downloadImage(url: String?, outputFileName: String?) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle(outputFileName)
        request.setDescription("Downloading $outputFileName")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.allowScanningByMediaScanner()
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outputFileName)
        val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

}