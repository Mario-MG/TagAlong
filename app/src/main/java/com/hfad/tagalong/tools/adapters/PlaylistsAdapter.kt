package com.hfad.tagalong.tools.adapters

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
    private val mPlaylists: ArrayList<CustomPlaylist>
    ) : RecyclerView.Adapter<PlaylistsAdapter.ViewHolder>() {

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView), View.OnClickListener {
        val nameTextView = itemView.findViewById<TextView>(R.id.playlist_name_tv)
        val sizeTextView = itemView.findViewById<TextView>(R.id.playlist_size_tv)
        val imageView = itemView.findViewById<ImageView>(R.id.playlist_image)
        private var playlist: CustomPlaylist? = null

        init {
            listItemView.setOnClickListener(this)
        }

        override fun onClick(listItemView: View) {
            val context = listItemView.context
            context.startActivity(Intent(
                context,
                TrackListActivity::class.java
            ).apply {
                putExtra(Extras.PLAYLIST_ID, playlist?.id)
            })
        }

        fun bindPlaylist(playlist: CustomPlaylist) {
            this.playlist = playlist
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val playlistView = inflater.inflate(R.layout.item_custom_playlist, parent, false)
        return ViewHolder(playlistView)
    }

    override fun onBindViewHolder(viewHolder: PlaylistsAdapter.ViewHolder, position: Int) {
        if (itemCount < CustomPlaylist.getTotalPlaylists() && position == itemCount-1) {
            makeLoadingTextVisible(true)
            getMorePlaylists()
            thread {
                Thread.sleep(500)
                activity.runOnUiThread {
                    makeLoadingTextVisible(false)
                }
            }
        }
        val playlist: CustomPlaylist = mPlaylists[position]
        viewHolder.bindPlaylist(playlist)
        val nameTextView = viewHolder.nameTextView
        nameTextView.text = playlist.name
        val sizeTextView = viewHolder.sizeTextView
        sizeTextView.text = sizeTextView.context.resources.getQuantityString(R.plurals.playlist_size, playlist.size, playlist.size)
        val imageView = viewHolder.imageView
        if (playlist.imageUrl !== null) {
            // TODO: Make image square
            Picasso.get().load(playlist.imageUrl).into(imageView)
        } else {
            imageView.setImageDrawable(null)
        }
    }

    override fun getItemCount(): Int {
        return mPlaylists.size
    }

    private fun getMorePlaylists() {
        // TODO: Añadir "Cargando" o algo por el estilo
        val oldItemCount = itemCount
        thread {
            val newPlaylists = CustomPlaylist.getAllPlaylistsFromApi(oldItemCount)
            activity.runOnUiThread {
                mPlaylists.addAll(newPlaylists)
                this.notifyItemRangeInserted(oldItemCount, itemCount)
            }
        }
    }

    private fun makeLoadingTextVisible(visible: Boolean = true) {
        val loadingTextView = activity.findViewById<TextView>(R.id.loading_tv)
        loadingTextView.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

}