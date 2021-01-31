package com.hfad.tagalong.views

import android.content.Context
import android.util.AttributeSet
import com.hfad.tagmanagerview.TagManagerView
import java.util.*

class CustomTagManagerForPlaylistCreationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : TagManagerView(context, attrs, defStyleAttr, defStyleRes) {
    init {
        addTagButton.visibility = GONE
    }

    override fun addItemToList(item: String) {
        if (tagList.indexOf(item) == -1) {
            super.addItemToList(item)
        }
    }

    override fun addTextAsItemToList() {}

    override fun preProcessText() {
        inputText = inputText.toLowerCase(Locale.ROOT)
    }

    override fun postProcessText() {
        inputText = ""
    }
}