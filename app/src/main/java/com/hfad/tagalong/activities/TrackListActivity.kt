package com.hfad.tagalong.activities

import android.os.Bundle
import android.view.View
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
    lateinit var tracks: List<CustomTrack>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_list)

        val playlistId = intent.getStringExtra(Extras.PLAYLIST_ID)
        // Lookup the recyclerview in activity layout
        val rvTracks = findViewById<View>(R.id.rvTracks) as RecyclerView
        thread {
            // Initialize tracks
            tracks = if (playlistId != null) {
                CustomTrack.createListOfTracks(playlistId)
                    .filter { track -> track.name != "null" }
            } else {
                val tagName = intent.getStringExtra(Extras.TAG_NAME)
                val dbHelper = DBHelper(this)
                val tracksWithTag = dbHelper.selectSongDataByTagNames(tagName!!)
                tracksWithTag
            }
            // Create Adapter passing in the tracks
            val adapter = TracksAdapter(this, tracks as ArrayList<CustomTrack>, playlistId)
            runOnUiThread {
                // Attach the Adapter to the RecyclerView to populate items
                rvTracks.adapter = adapter
                // Set layout manager to position the items
                rvTracks.layoutManager = LinearLayoutManager(this)
            }
        }
    }
}