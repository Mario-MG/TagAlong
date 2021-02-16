package com.hfad.tagalong.tools.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.activities.TrackListActivity
import com.hfad.tagalong.config.Extras
import com.hfad.tagalong.types.CustomPlaylist
import com.squareup.picasso.Picasso
import kotlin.concurrent.thread

// Source: https://guides.codepath.com/android/using-the-recyclerview
class PlaylistsAdapter (
    private val activity: FragmentActivity,
    private val playlists: ArrayList<CustomPlaylist>
) : RecyclerView.Adapter<PlaylistsAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val nameTextView = itemView.findViewById<TextView>(R.id.playlist_name_tv)
        val sizeTextView = itemView.findViewById<TextView>(R.id.playlist_size_tv)
        val imageView = itemView.findViewById<ImageView>(R.id.playlist_image)

        private val context = listItemView.context

        private lateinit var playlist: CustomPlaylist

        init {
            listItemView.setOnClickListener {
                onClickListItemView()
            }
        }

        private fun onClickListItemView() {
            startTrackListActivity()
        }

        private fun startTrackListActivity() {
            val trackListIntent = Intent(context, TrackListActivity::class.java).apply {
                putExtra(Extras.PLAYLIST_ID, playlist.id)
            }
            context.startActivity(trackListIntent)
        }

        fun bindPlaylist(playlist: CustomPlaylist) {
            this.playlist = playlist
            populate()
        }

        private fun populate() {
            populateNameTextView()
            populateSizeTextView()
            populateImageView()
        }

        private fun populateNameTextView() {
            nameTextView.text = playlist.name
        }

        private fun populateSizeTextView() {
            sizeTextView.text = sizeTextView.context.resources.getQuantityString(
                R.plurals.playlist_size,
                playlist.size,
                playlist.size
            )
        }

        private fun populateImageView() {
            if (playlist.imageUrl !== null) {
                // TODO: Make image square
                Picasso.get().load(playlist.imageUrl).into(imageView)
            } else {
                imageView.setImageDrawable(null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val playlistView = inflater.inflate(R.layout.item_custom_playlist, parent, false)
        return ViewHolder(playlistView)
    }

    override fun onBindViewHolder(viewHolder: PlaylistsAdapter.ViewHolder, position: Int) {
        if (areThereMorePlaylistsToLoad() && isLastItem(position)) {
            loadMorePlaylists()
        }
        viewHolder.bindPlaylist(playlists[position])
    }

    private fun areThereMorePlaylistsToLoad() = itemCount < CustomPlaylist.getTotalPlaylists()

    private fun isLastItem(position: Int) = position == itemCount - 1

    private fun loadMorePlaylists() {
        setLoadingTextVisibility(true)
        getAndAddMorePlaylists()
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

    private fun getAndAddMorePlaylists() {
        thread {
            val oldItemCount = itemCount
            val newPlaylists = CustomPlaylist.getAllPlaylistsFromApi(oldItemCount)
            activity.runOnUiThread {
                playlists.addAll(newPlaylists)
                this.notifyItemRangeInserted(oldItemCount, itemCount)
            }
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}