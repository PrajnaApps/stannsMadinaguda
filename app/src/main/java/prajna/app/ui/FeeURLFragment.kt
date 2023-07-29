package prajna.app.ui

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.JsonParser
import com.qlx.myviejas.utills.sharedPreferences.UserSession
import org.json.JSONObject
import prajna.app.R
import prajna.app.utills.BASE_URL
import prajna.app.utills.FEE_URL
import prajna.app.utills.customFonts.Text_Bold
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 */
class FeeURLFragment : Fragment(){


    private var mTokenData: String=""
    private var mStudentKey: String=""
    private lateinit var llBack: LinearLayout
    private lateinit var ivBack: ImageView
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var tvLogout: Text_Bold
    private lateinit var webView: WebView
    private lateinit var pb_web: ProgressBar
    private lateinit var mProgress: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.webview_fee, container, false)

        mTokenData = UserSession(requireActivity()).getStudentKey()
        mProgress = ProgressDialog(requireActivity())
        mProgress.isIndeterminate = true

        var dict_filterdata = JSONObject()
        try {
            dict_filterdata = JSONObject(mTokenData)
            mStudentKey = dict_filterdata.getString("stud_key")

        } catch (e: Exception) {
            e.printStackTrace()
        }
        initView(rootView)


        return rootView
    }


    private fun initView(v: View) {
        llBack = v.findViewById(R.id.llback)
        ivBack = v.findViewById(R.id.ivBack)
        tvHeadertitle = v.findViewById(R.id.tvHeadertitle)
        tvLogout = v.findViewById(R.id.tvLogout)
        webView = v.findViewById(R.id.webview)

        tvLogout.visibility = View.VISIBLE
        tvHeadertitle.setText("Fee Details")
        tvLogout.setText("Fee History")
        llBack.setOnClickListener(View.OnClickListener {

        })

//        ivBack.visibility = View.VISIBLE




        // loading http://www.google.com url in the WebView.
        val mURL=FEE_URL+"Home/StudentFeeData/"+mStudentKey
//        Log.e("TAG", "url**: "+mURL )
        webView.loadUrl(mURL)

        // this will enable the javascript.
        webView.settings.javaScriptEnabled = true

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                mProgress.setMessage("Loading...")
                mProgress.show()
                if (progress == 100) {
                    mProgress.dismiss()
                }
            }
        }
//        webView.setWebViewClient(object : WebViewClient() {
//            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//                pb_web.visibility=View.VISIBLE
//                webView.visibility=View.GONE
//                super.onPageStarted(view, url, favicon)
//            }
//
//            override fun onPageFinished(view: WebView, url: String) {
//                pb_web.visibility=View.GONE
//                webView.visibility=View.VISIBLE
//                super.onPageFinished(view, url)
//            }
//        })


        tvLogout.setOnClickListener(View.OnClickListener {

            startActivity(Intent(requireActivity(), PaymentHistory::class.java))
            requireActivity().overridePendingTransition(
                R.anim.move_left_enter,
                R.anim.move_left_exit
            )
            requireActivity().finish()
        })

    }
    private fun getChromeClient(): WebChromeClient {
        return object : WebChromeClient() {

        }
    }
    private fun getBitmapInputStream(bitmap: Bitmap, compressFormat: Bitmap.CompressFormat): InputStream {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(compressFormat, 80, byteArrayOutputStream)
        val bitmapData: ByteArray = byteArrayOutputStream.toByteArray()
        return ByteArrayInputStream(bitmapData)
    }

    private fun getClient(): WebViewClient {
        return object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                return super.shouldInterceptRequest(view, request)
            }

            @RequiresApi(Build.VERSION_CODES.R)
            override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
                if(url == null){
                    return super.shouldInterceptRequest(view, url as String)
                }
                return if(url.toLowerCase(Locale.ROOT).contains(".jpg") || url.toLowerCase(Locale.ROOT).contains(".jpeg")){
                    val bitmap = Glide.with(webView).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).load(url).submit().get()
                    WebResourceResponse("image/jpg", "UTF-8",getBitmapInputStream(bitmap,
                        Bitmap.CompressFormat.JPEG))
                }else if(url.toLowerCase(Locale.ROOT).contains(".png")){
                    val bitmap = Glide.with(webView).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).load(url).submit().get()
                    WebResourceResponse("image/png", "UTF-8",getBitmapInputStream(bitmap,
                        Bitmap.CompressFormat.PNG))
                }else if(url.toLowerCase(Locale.ROOT).contains(".webp")){
                    val bitmap = Glide.with(webView).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).load(url).submit().get()
                    WebResourceResponse("image/webp", "UTF-8",getBitmapInputStream(bitmap,
                        Bitmap.CompressFormat.WEBP_LOSSY))
                }else{
                    super.shouldInterceptRequest(view, url)
                }

            }
        }
    }




}
