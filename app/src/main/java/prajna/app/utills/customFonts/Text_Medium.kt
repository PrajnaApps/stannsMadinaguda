package prajna.app.utills.customFonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet


class Text_Medium: androidx.appcompat.widget.AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        if (!isInEditMode) {
            val tf = Typeface.createFromAsset(context.assets, "poppins_medium.ttf")
            typeface = tf
        }
    }

}