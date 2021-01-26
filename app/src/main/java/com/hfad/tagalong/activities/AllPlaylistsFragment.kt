package com.hfad.tagalong.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.adapters.PlaylistsAdapter
import com.hfad.tagalong.types.CustomPlaylist
import kotlin.concurrent.thread

class AllPlaylistsFragment : Fragment() {
    private lateinit var playlists: ArrayList<CustomPlaylist>
    private lateinit var rvPlaylists: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_playlists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPlaylists = view.findViewById(R.id.rvPlaylists)
    }

    override fun onResume() {
        super.onResume()

        thread {
            // Initialize playlists
            playlists = CustomPlaylist.createListOfPlaylists()
            // Create Adapter passing in the playlists
            val adapter = PlaylistsAdapter(activity!!, playlists)
            activity?.runOnUiThread {
                // Attach the Adapter to the RecyclerView to populate items
                rvPlaylists.adapter = adapter
                // Set layout manager to position the items
                rvPlaylists.layoutManager = LinearLayoutManager(activity)
            }
        }
    }
}