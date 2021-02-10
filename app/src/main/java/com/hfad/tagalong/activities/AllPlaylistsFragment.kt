package com.hfad.tagalong.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.adapters.PlaylistsAdapter
import com.hfad.tagalong.types.CustomPlaylist
import kotlin.concurrent.thread

class AllPlaylistsFragment : Fragment() {
    private lateinit var mActivity: FragmentActivity

    private lateinit var playlists: ArrayList<CustomPlaylist>
    private lateinit var playlistsRecyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_playlists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePlaylistsRecyclerView(view)
    }

    private fun initializePlaylistsRecyclerView(view: View) {
        playlistsRecyclerView = view.findViewById(R.id.playlists_recyclerview)
    }

    override fun onResume() {
        super.onResume()
        populatePlaylistsRecyclerView()
    }

    private fun populatePlaylistsRecyclerView() {
        thread {
            playlists = CustomPlaylist.getAllPlaylistsFromApi()
            setPlaylistsIntoRecyclerView()
        }
    }

    private fun setPlaylistsIntoRecyclerView() {
        val adapter = PlaylistsAdapter(mActivity, playlists)
        mActivity.runOnUiThread {
            playlistsRecyclerView.adapter = adapter
            playlistsRecyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }
}