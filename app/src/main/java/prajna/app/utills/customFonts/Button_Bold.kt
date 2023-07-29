package prajna.app.utills.customFonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.Button


class Button_Bold : androidx.appcompat.widget.AppCompatButton {

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
            val tf = Typeface.createFromAsset(context.assets, "poppins_bold.ttf")
            typeface = tf
        }
    }

}