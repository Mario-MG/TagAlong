package com.hfad.tagalong.tools.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.DBHelper

class TagViewAdapter(
    private val activity: Activity,
    private val songId: String,
    private val mTags: ArrayList<String>
    ) : RecyclerView.Adapter<TagViewAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.tagName_TextView)
        val imageView = itemView.findViewById<ImageView>(R.id.tag_view_close_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val tagView = inflater.inflate(R.layout.item_tag_view, parent, false)
        return ViewHolder(tagView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val tagName = mTags[position]
        val textView = viewHolder.nameTextView
        textView.text = "#$tagName"
        val imageView = viewHolder.imageView
        imageView.setOnClickListener {
            val dbHelper = DBHelper(activity)
            dbHelper.deleteSongWithTag(songId, tagName)
            val newPosition = mTags.indexOf(tagName)
            mTags.remove(tagName)
            notifyItemRemoved(newPosition)
        }
    }

    override fun getItemCount(): Int {
        return mTags.size
    }

    fun addItem(tagName: String) {
        mTags.add(tagName)
        notifyItemInserted(itemCount)
    }
}