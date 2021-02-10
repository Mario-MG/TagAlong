package com.hfad.tagalong.tools.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.hfad.tagalong.R
import com.hfad.tagalong.activities.SingleTrackTaggingActivity
import com.hfad.tagalong.config.Extras
import com.hfad.tagalong.types.CustomTrack
import com.squareup.picasso.Picasso
import kotlin.concurrent.thread

// Source: https://guides.codepath.com/android/using-the-recyclerview
class TracksAdapter (
    private val activity: Activity,
    private val mTracks: ArrayList<CustomTrack>,
    private val playlistId: String?
    ) : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView), View.OnClickListener {
        val nameTextView = itemView.findViewById<TextView>(R.id.track_name_tv)
        val infoTextView = itemView.findViewById<TextView>(R.id.track_info_tv)
        val imageView = itemView.findViewById<ImageView>(R.id.track_image)
        private lateinit var track: CustomTrack

        init {
            listItemView.setOnClickListener(this)
        }

        override fun onClick(listItemView: View) {
            val context = listItemView.context
            val trackData = Gson().toJson(track)
            context.startActivity(Intent(
                context,
                SingleTrackTaggingActivity::class.java
            ).apply {
                putExtra(Extras.TRACK_DATA, trackData)
            })
        }

        fun bindTrack(track: CustomTrack) {
            this.track = track
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val trackView = inflater.inflate(R.layout.item_custom_track, parent, false)
        return ViewHolder(trackView)
    }

    override fun onBindViewHolder(viewHolder: TracksAdapter.ViewHolder, position: Int) {
        playlistId?.let {
            if (itemCount < CustomTrack.getTotalTracksForPlaylistId(playlistId) && position == itemCount - 1) {
                makeLoadingTextVisible(true)
                getMoreTracks(playlistId)
                thread {
                    Thread.sleep(500)
                    activity.runOnUiThread {
                        makeLoadingTextVisible(false)
                    }
                }
            }
        }
        val track: CustomTrack = mTracks[position]
        viewHolder.bindTrack(track)
        val textView = viewHolder.nameTextView
        textView.text = track.name
        val infoView = viewHolder.infoTextView
        // TODO: Handle more than one artist
        infoView.text = infoView.context.resources.getString(R.string.track_info, if (track.artists.isNotEmpty()) track.artists[0] else "null", track.album)
        val imageView = viewHolder.imageView
        if (track.imageUrl !== null) {
            // TODO: Make image square
            Picasso.get().load(track.imageUrl).into(imageView)
        } else {
            imageView.setImageDrawable(null)
        }
    }

    override fun getItemCount(): Int {
        return mTracks.size
    }

    private fun getMoreTracks(playlistId: String) {
        // TODO: Añadir "Cargando" o algo por el estilo
        val oldItemCount = itemCount
        thread {
            val newTracks = CustomTrack.getTracksFromApi(playlistId, oldItemCount)
            activity.runOnUiThread {
                mTracks.addAll(newTracks)
                this.notifyItemRangeInserted(oldItemCount, itemCount)
            }
        }
    }

    private fun makeLoadingTextVisible(visible: Boolean = true) {
        val loadingTextView = activity.findViewById<TextView>(R.id.loading_tv)
        loadingTextView.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

}