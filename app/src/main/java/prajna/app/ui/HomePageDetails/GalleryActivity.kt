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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.ads.mediationtestsuite.activities.HomeActivity
import okhttp3.ResponseBody
import org.json.JSONArray
import prajna.app.R
import prajna.app.repository.api.ApiServices
import prajna.app.repository.model.GalleryModel
import prajna.app.repository.retrofitservice.ServiceBuilder
import prajna.app.ui.Homescreen
import prajna.app.ui.adapter.GalleryAdapter
import prajna.app.utills.DividerItemDecorator
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class GalleryActivity : AppCompatActivity() {

    private var mFeedback: String = ""
    private var mHallticket: String = ""
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var tvEmpty: Text_Medium
    private lateinit var rv_gallery: RecyclerView
    private val galleryList: ArrayList<GalleryModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_layout)


        initialise()

        getGallery()
    }

    private fun initialise() {
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)
        rv_gallery = findViewById(R.id.rv_gallery)
        tvEmpty = findViewById(R.id.tvEmpty)

        ivBack.visibility=View.VISIBLE
        tvHeadertitle.text = "Gallery"

        val layoutmanager = GridLayoutManager(this@GalleryActivity,3)
        rv_gallery.layoutManager = layoutmanager
        rv_gallery.isNestedScrollingEnabled = false
//        rv_history.addItemDecoration(Divdivider.xmliderItemDecoration(rv_history.getContext(), DividerItemDecoration.VERTICAL))
//        val dividerItemDecoration: RecyclerView.ItemDecoration =
//            DividerItemDecorator(
//                ContextCompat.getDrawable(
//                    rv_gallery.context,
//                    R.drawable.divider
//                )
//            )
//        rv_gallery.addItemDecoration(dividerItemDecoration)

        llback.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

    }

    private fun getGallery() {

//        pbar.visibility = View.VISIBLE
        val retrofitService: ApiServices =
            ServiceBuilder(this@GalleryActivity).getRetrofit()!!.create()
        retrofitService.getGallery().enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.code() == 200) {
                    val string = response.body()!!.string()


                    val jsonarray = JSONArray(string)
                    if(jsonarray.length()>0){
                        Log.e("TAG", "gall**** : " + jsonarray.length())

                        for (i in 0 until jsonarray.length() ){
                            val jsonobj = jsonarray.getJSONObject(i)
                            val ImageUrl=jsonobj.getString("ImagePath")
                            val name=jsonobj.getString("ImageName")

                            galleryList.add(GalleryModel(ImageUrl,name))

//                        Log.e("TAG", "pay_hisptory**** : " + jsonobj.getString("SystemReceiptNo"))

                        }
                        val adapter = GalleryAdapter(this@GalleryActivity, galleryList)
                        rv_gallery.adapter = adapter
                        tvEmpty.visibility=View.GONE
                        rv_gallery.visibility=View.VISIBLE

                    }else{
                        tvEmpty.visibility=View.VISIBLE
                        rv_gallery.visibility=View.GONE

                    }


                }else{
                    tvEmpty.visibility=View.VISIBLE
                    rv_gallery.visibility=View.GONE
                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@GalleryActivity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }



    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Homescreen::class.java))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }
}
