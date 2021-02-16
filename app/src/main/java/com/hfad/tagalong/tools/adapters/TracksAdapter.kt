package com.hfad.tagalong.tools.adapters

import android.app.Activity
import android.content.Context
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
class TracksAdapter(
    private val activity: Activity,
    private val mTracks: ArrayList<CustomTrack>,
    private val playlistId: String?
) : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.track_name_tv)
        val infoTextView = itemView.findViewById<TextView>(R.id.track_info_tv)
        val imageView = itemView.findViewById<ImageView>(R.id.track_image)

        private val context = listItemView.context

        private lateinit var track: CustomTrack

        init {
            listItemView.setOnClickListener {
                onClickViewHolder()
            }
        }

        private fun onClickViewHolder() {
            startSingleTrackTaggingActivity()
        }

        private fun startSingleTrackTaggingActivity() {
            val trackData = Gson().toJson(track)
            val singleTrackTaggingIntent = Intent(context, SingleTrackTaggingActivity::class.java)
                .apply { putExtra(Extras.TRACK_DATA, trackData) }
            context.startActivity(singleTrackTaggingIntent)
        }

        fun bindTrack(track: CustomTrack) {
            this.track = track
            populate(track)
        }

        private fun populate(track: CustomTrack) {
            populateNameTextView(track)
            populateInfoTextView(track)
            populateImageView(track)
        }

        private fun populateNameTextView(track: CustomTrack) {
            nameTextView.text = track.name
        }

        private fun populateInfoTextView(track: CustomTrack) {
            // TODO: Handle more than one artist
            infoTextView.text = context.resources.getString(
                R.string.track_info,
                if (track.artists.isNotEmpty()) track.artists[0] else "null",
                track.album
            )
        }

        private fun populateImageView(track: CustomTrack) {
            if (track.imageUrl !== null) {
                // TODO: Make image square
                Picasso.get().load(track.imageUrl).into(imageView)
            } else {
                imageView.setImageDrawable(null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val trackView = inflater.inflate(R.layout.item_custom_track, parent, false)
        return ViewHolder(trackView)
    }

    override fun onBindViewHolder(viewHolder: TracksAdapter.ViewHolder, position: Int) {
        if (playlistId != null && areThereMoreTracksToLoad() && isLastItem(position)) {
            loadMoreTracks()
        }
        viewHolder.bindTrack(mTracks[position])
    }

    private fun areThereMoreTracksToLoad(): Boolean {
        return playlistId == null ||
            itemCount < CustomTrack.getTotalTracksForPlaylistId(playlistId)
    }

    private fun isLastItem(position: Int) = position == itemCount-1

    private fun loadMoreTracks() {
        if (playlistId == null) {
            return
        }
        setLoadingTextVisibility(true)
        getMoreTracks(playlistId)
        thread {
            Thread.sleep(500)
            activity.runOnUiThread {
                setLoadingTextVisibility(false)
            }
        }
    }

    private fun setLoadingTextVisibility(visible: Boolean = true) {
        val loadingTextView = activity.findViewById<TextView>(R.id.loading_tv)
        loadingTextView.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    private fun getMoreTracks(playlistId: String) {
        val oldItemCount = itemCount
        thread {
            val newTracks = CustomTrack.getTracksFromApi(playlistId, oldItemCount)
            activity.runOnUiThread {
                mTracks.addAll(newTracks)
                this.notifyItemRangeInserted(oldItemCount, itemCount)
            }
        }
    }

    override fun getItemCount(): Int {
        return mTracks.size
    }
}