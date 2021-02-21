package com.hfad.tagalong.views

import android.content.Context
import android.util.AttributeSet
import com.hfad.tagmanagerview.TagManagerView

class TagManagerForPlaylistRuleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : TagManagerView(context, attrs, defStyleAttr, defStyleRes) {
    init {
        addTagButton.visibility = GONE
        addTagEditText.visibility = GONE
    }

    override fun addItemToList(item: String) {}

    override fun addTextAsItemToList() {}

    override fun preProcessText() {}

    override fun postProcessText() {}

    override fun onClickCloseIcon(position: Int, tagName: String) {}
}