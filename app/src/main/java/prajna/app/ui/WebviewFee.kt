package prajna.app.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import prajna.app.R
import prajna.app.utills.customFonts.Text_Bold
import prajna.app.utills.customFonts.Text_Medium
import java.util.Locale

import android.graphics.Bitmap.CompressFormat
import android.webkit.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*


class WebviewFee : AppCompatActivity() {

    private lateinit var webView: WebView

    private var mTokenData: String=""
    private var mStudentKey: String=""
    private var mYearCode: String=""
    private lateinit var ivBack: ImageView
    private lateinit var llback: LinearLayout
    private lateinit var tvHeadertitle: Text_Bold
    private lateinit var rv_assignment: RecyclerView
    private lateinit var tvEmpty: Text_Medium
    private lateinit var pb_web: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webview_fee)

        initialise()

    }

    private fun initialise() {
        llback = findViewById(R.id.llback)
        ivBack = findViewById(R.id.ivBack)
        tvHeadertitle = findViewById(R.id.tvHeadertitle)
        webView = findViewById(R.id.webview)
        tvHeadertitle.setText("Fee")
        ivBack.visibility = View.VISIBLE

        // loading http://www.google.com url in the WebView.
        webView.loadUrl("https://madeenagudaicsefee.stannfinedu.com/Home/ParentLogin")

        // this will enable the javascript.
        webView.settings.javaScriptEnabled = true

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.webViewClient = WebViewClient()

        ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

    }

    private fun getChromeClient(): WebChromeClient {
        return object : WebChromeClient() {

        }
    }

    private fun getBitmapInputStream(bitmap:Bitmap,compressFormat: CompressFormat):InputStream{
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@WebviewFee, Homescreen::class.java).putExtra("id","history"))
        overridePendingTransition(R.anim.move_right_enter, R.anim.move_right_exit)
    }
}