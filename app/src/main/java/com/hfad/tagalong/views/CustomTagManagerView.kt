package com.hfad.tagalong.views

import android.content.Context
import android.util.AttributeSet
import com.hfad.tagmanagerview.TagManagerView
import java.util.*

class CustomTagManagerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : TagManagerView(context, attrs, defStyleAttr, defStyleRes) {

    override fun preProcessText() {
        text = text.toLowerCase(Locale.ROOT)
    }

    override fun postProcessText() {
        // TODO: Insertar tag en BD
        text = ""
    }

    override fun onClickCloseIcon(position: Int, tagName: String) {
        // TODO: Borrar tag de BD
    }
}