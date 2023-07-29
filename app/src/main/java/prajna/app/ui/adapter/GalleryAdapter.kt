package prajna.app.ui.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import prajna.app.R
import prajna.app.repository.model.GalleryModel
import prajna.app.ui.HomePageDetails.GalleryActivity
import prajna.app.ui.Homescreen
import prajna.app.utills.SelectableRoundedImageView
import prajna.app.utills.customFonts.Text_Normal
import java.util.*

class GalleryAdapter(var context: Context, var dataList: ArrayList<GalleryModel>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivGallery: ImageView = view.findViewById(R.id.ivGallery)
        var tvGalleryName: Text_Normal = view.findViewById(R.id.tvGalleryName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        Glide.with(context).load(data.ImagePath)
            .into(holder.ivGallery)
        holder.tvGalleryName.setText(data.ImageName)

        holder.ivGallery.setOnClickListener(View.OnClickListener {
            openProfileImagePreview(data.ImagePath!!)

        })



    }
    @SuppressLint("ResourceType")
    fun openProfileImagePreview(profileImage: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.decorView?.setBackgroundResource(android.graphics.Color.TRANSPARENT)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_image)

        val ivPreview =
            dialog.findViewById<View>(R.id.iv_preview_image) as SelectableRoundedImageView

        Glide.with(context).load(profileImage).placeholder(R.drawable.ic_avatar)
            .into(ivPreview)
        dialog.show()
    }


    override fun getItemCount(): Int {
        return dataList.size
    }
}