package com.hfad.tagalong.tools.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.activities.TrackListActivity
import com.hfad.tagalong.config.Extras

// Source: https://guides.codepath.com/android/using-the-recyclerview
class TagsAdapter (private val mTags: List<String>) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView), View.OnClickListener {
        val nameTextView = itemView.findViewById<TextView>(R.id.tag_name_tv)
        private var tag: String? = null

        init {
            listItemView.setOnClickListener(this)
        }

        override fun onClick(listItemView: View) {
            val context = listItemView.context
            context.startActivity(Intent(
                context,
                TrackListActivity::class.java
            ).apply {
                putExtra(Extras.TAG_NAME, tag)
            })
        }

        fun bindPlaylist(tag: String) {
            this.tag = tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val tagView = inflater.inflate(R.layout.item_tag, parent, false)
        return ViewHolder(tagView)
    }

    override fun onBindViewHolder(viewHolder: TagsAdapter.ViewHolder, position: Int) {
        val tag: String = mTags[position]
        viewHolder.bindPlaylist(tag)
        val nameTextView = viewHolder.nameTextView
        nameTextView.text = tag
    }

    override fun getItemCount(): Int {
        return mTags.size
    }

}