package com.hfad.tagalong.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.config.Extras
import com.hfad.tagalong.tools.DBHelper
import com.hfad.tagalong.tools.adapters.TracksAdapter
import com.hfad.tagalong.types.CustomTrack
import kotlin.concurrent.thread


class TrackListActivity : AppCompatActivity() {
    private lateinit var tracksRecyclerView: RecyclerView

    private var playlistId: String? = null
    private var tagName: String? = null
    private lateinit var tracks: ArrayList<CustomTrack>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_list)

        getPlaylistIdOrTagName()
        initializeTracksAndRecyclerView()
    }

    private fun getPlaylistIdOrTagName() {
        playlistId = intent.getStringExtra(Extras.PLAYLIST_ID)
        if (playlistId == null) {
            tagName = intent.getStringExtra(Extras.TAG_NAME)
        }
    }

    private fun initializeTracksAndRecyclerView() {
        thread {
            initializeTracks()
            initializeTracksRecyclerView()
        }
    }

    private fun initializeTracks() {
        tracks = if (playlistId != null) {
            getTracksForPlaylistId()
        } else {
            getTracksForTagName()
        }
    }

    private fun getTracksForPlaylistId(): ArrayList<CustomTrack> {
        val allTracks = CustomTrack.getTracksFromApi(playlistId!!)
        return allTracks.filter { track -> track.name != "null" } as ArrayList<CustomTrack>
    }

    private fun getTracksForTagName(): ArrayList<CustomTrack> {
        val dbHelper = DBHelper(this)
        val tracksWithTag = dbHelper.selectSongDataByTagNames(tagName!!)
        dbHelper.close()
        return tracksWithTag
    }

    private fun initializeTracksRecyclerView() {
        tracksRecyclerView = findViewById(R.id.tracks_recyclerview)
        val adapter = TracksAdapter(this, tracks, playlistId)
        runOnUiThread {
            tracksRecyclerView.adapter = adapter
            tracksRecyclerView.layoutManager = LinearLayoutManager(this)
        }
    }
}