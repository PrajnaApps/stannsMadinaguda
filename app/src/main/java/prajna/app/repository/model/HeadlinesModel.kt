package prajna.app.repository.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class HeadlinesModel(
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("short_description")
    var description: String? = null
) : Serializable

