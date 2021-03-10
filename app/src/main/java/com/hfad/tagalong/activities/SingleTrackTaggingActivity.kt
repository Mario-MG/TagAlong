package com.hfad.tagalong.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.gson.Gson
import com.hfad.tagalong.R
import com.hfad.tagalong.config.Extras
import com.hfad.tagalong.tools.DBHelper
import com.hfad.tagalong.types.CustomTrack
import com.hfad.tagalong.views.TagManagerForSingleTrackView
import com.squareup.picasso.Picasso
import kotlin.concurrent.thread

class SingleTrackTaggingActivity : AppCompatActivity() {
    private lateinit var trackImageView: ImageView
    private lateinit var trackNameTextView: TextView
    private lateinit var trackInfoTextView: TextView
    private lateinit var tagManagerView: TagManagerForSingleTrackView

    private lateinit var track: CustomTrack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_track_tagging)
        initializeUI()
    }

    private fun initializeUI() {
        initializeTrack()
        initializeImageView()
        initializeTrackNameTextView()
        initializeTrackInfoTextView()
        initializeTagManagerView()
    }

    private fun initializeTrack() {
        val trackData = intent.getStringExtra(Extras.TRACK_DATA)
        track = Gson().fromJson(trackData, CustomTrack::class.java)
    }

    private fun initializeImageView() {
        trackImageView = findViewById(R.id.track_image)
        track.imageUrl?.let { Picasso.get().load(it).into(trackImageView) }
    }

    private fun initializeTrackNameTextView() {
        trackNameTextView = findViewById(R.id.track_name_tv)
        trackNameTextView.text = track.name
        trackNameTextView.ellipsize = TextUtils.TruncateAt.END
    }

    private fun initializeTrackInfoTextView() {
        trackInfoTextView = findViewById(R.id.track_info_tv)
        trackInfoTextView.text = resources.getString(
            R.string.track_info,
            if (track.artists.isNotEmpty()) track.artists[0] else "null",
            track.album
        )
        trackInfoTextView.ellipsize = TextUtils.TruncateAt.END
    }

    private fun initializeTagManagerView() {
        tagManagerView = findViewById(R.id.single_track_tag_manager)
        tagManagerView.track = this.track
        populateTagManagerView()
    }

    private fun populateTagManagerView() {
        thread {
            val dbHelper = DBHelper(this)
            val currentTags = dbHelper.selectTagNamesBySongId(track.id)
            val allTags = dbHelper.selectAllTags()
            dbHelper.close()
            runOnUiThread {
                tagManagerView.tagList = currentTags
                tagManagerView.autoCompleteTagList = allTags
            }
        }
    }
}