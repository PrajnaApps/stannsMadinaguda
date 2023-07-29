package prajna.app.utills

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.*
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import prajna.app.R
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object Utilities {

    private const val maxHeight = 1280.0f
    private const val maxWidth = 1280.0f
    private lateinit var mProgress: ProgressDialog

    /*
        Internet Availability check

         */


    /**
     * Method to hide keyboard start
     *
     * @param activity
     */
    fun hideSoftKeyboard(activity: Activity) {

        val inputMethodManager = activity
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            try {
                inputMethodManager.hideSoftInputFromWindow(
                    activity
                        .currentFocus!!.windowToken, 0
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
    }


    fun getItemList(getItemAsList: ArrayList<String>, jsonResponse: String): ArrayList<String> {

        val list = ArrayList<String>()

        val subStringValue = 2
        var name: String

        var response = jsonResponse.replace("\\u0022", "")
        response = response.substring(subStringValue, response.length - subStringValue)
        val responseSplitList =
            response.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        if (responseSplitList.size > 0) {
            list.add("Select")
            for (listItem in responseSplitList) {
                val itemNameList =
                    listItem.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                name = itemNameList[1]
                name = name.replace("}", "")
                name = name.replace("\"", "")
                list.add(name)
            }
        }

        return list


    }


    fun showSoftKeyboard(activity: Activity, editText: EditText) {
        val inputMethodManager = activity
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }


    fun hideStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            /*window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.transparent));*/
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            // Show status bar
            //            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        }
    }

    //    fun startShakeAnimation(context: Activity, view: View) {
//        val shake = AnimationUtils.loadAnimation(context, R.anim.shake)
//        view.startAnimation(shake)
//    }
//
//    fun startBubleAnimation(context: Activity, view: View) {
//        val buble = AnimationUtils.loadAnimation(context, R.anim.bubble_animation)
//        view.startAnimation(buble)
//    }
//
//    fun startSlideUpAnimation(context: Activity, view: View) {
//        val slide_up = AnimationUtils.loadAnimation(context, R.anim.slide_up)
//        view.startAnimation(slide_up)
//    }
//
//    fun startSlideDownAnimation(context: Activity, view: View) {
//        val slide_down = AnimationUtils.loadAnimation(context, R.anim.slide_down)
//        view.startAnimation(slide_down)
//    }
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    fun compressImage(imagePath: String?): String? {

        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = false
        var bmp = BitmapFactory.decodeFile(imagePath, options)
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio: Float =
            maxWidth / maxHeight
        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth.toInt()
            } else {
                actualHeight = maxHeight.toInt()
                actualWidth = maxWidth.toInt()
            }
        }
        options.inSampleSize = calculateInSampleSize(
            options,
            actualWidth,
            actualHeight
        )
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            bmp = BitmapFactory.decodeFile(imagePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp!!,
            middleX - bmp.width / 2,
            middleY - bmp.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        if (bmp != null) {
            bmp.recycle()
        }
        val exif: ExifInterface
        try {
            exif = ExifInterface(imagePath!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            scaledBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var out: FileOutputStream? = null
        val filepath: String? = imagePath//getFilename();
        try {
            //new File(imageFilePath).delete();
            out = FileOutputStream(filepath)

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 30, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filepath
    }

    fun settingLocale(context: Activity, language: String) {
        val locale: Locale
        val resources = context.resources
        val config = Configuration(resources.configuration)
        if (language != "eng")
            locale = Locale(language, "IN")
        else
            locale = Locale(language)

        Locale.setDefault(locale)
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun displayToast(ctxt: Context, toastMessage: String) {
        val toast = Toast.makeText(ctxt, toastMessage, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)

        val group = toast.view as ViewGroup
        val messageTextView = group.getChildAt(0) as TextView
        messageTextView.textSize = 22f

        toast.show()
    }

    /**
     * Displaying snackbar
     * SUJITH DASARI
     * 04-12-2018
     */

    fun showMessage(view: View, msg: String) {
        try {
            val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG)

            val snack_view = snackbar.view
            //            android.support.design.R.id
            val tv =
                snack_view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            tv.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                view.resources.getDimension(R.dimen.fab_margin)
            )
            snackbar.show()
        } catch (e: Exception) {
            Log.v("Exception", "Exception$e")
        }

    }


    /*Call Intent*/
    fun shoeDialer(view: Context, number: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$number")
        view.startActivity(intent)
    }

    /**
     * SpannableString
     * SUJITH DASARI
     * 04-12-2018
     */

    fun getCustomString(name: String): SpannableString {
        val ss1 = SpannableString(name)
        ss1.setSpan(RelativeSizeSpan(1.5f), 0, name.length, 0)
        return ss1
    }

    /**
     * imageRotate
     * SUJITH DASARI
     * 04-12-2018
     */

    fun imageRotate(bitmapImage: Bitmap, path: String): Bitmap {
        var bitmapImage = bitmapImage
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(path)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (exif != null) {
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
            val m = Matrix()
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                m.postRotate(180f)
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                m.postRotate(90f)
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                m.postRotate(270f)
            }
            bitmapImage = Bitmap.createBitmap(
                bitmapImage,
                0,
                0,
                bitmapImage.width,
                bitmapImage.height,
                m,
                true
            )
        }
        return bitmapImage
    }


    /*Calcualte Time*/
    fun formatSeconds(timeInSeconds: Long): String {
        val hours = (timeInSeconds / 3600).toInt()
        val secondsLeft = (timeInSeconds - hours * 3600).toInt()
        val minutes = secondsLeft / 60
        val seconds = secondsLeft - minutes * 60

        var formattedTime = ""
        /*if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";*/

        if (hours > 0)
            formattedTime += hours.toString() + "h "

        formattedTime += minutes.toString() + "m "
        formattedTime += seconds.toString() + "s"

        /*if (minutes < 10)
                formattedTime += "0";
            formattedTime += minutes + ":";

            if (seconds < 10)
                formattedTime += "0";
            formattedTime += seconds ;*/

        return formattedTime
    }


}
