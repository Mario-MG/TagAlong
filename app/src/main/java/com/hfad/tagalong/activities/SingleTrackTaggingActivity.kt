package com.hfad.tagalong.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.gson.Gson
import com.hfad.tagalong.R
import com.hfad.tagalong.config.Extras
import com.hfad.tagalong.tools.DBHelper
import com.hfad.tagalong.tools.adapters.TagViewAdapter
import com.hfad.tagalong.types.CustomTrack
import com.hfad.tagalong.views.CustomTagManagerForSingleTrackView
import com.squareup.picasso.Picasso
import kotlin.concurrent.thread

class SingleTrackTaggingActivity : AppCompatActivity() {
    private lateinit var currentTrack: CustomTrack
    private lateinit var adapter: TagViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_track_tagging)

        val trackData = intent.getStringExtra(Extras.TRACK_DATA)
        currentTrack = Gson().fromJson(trackData, CustomTrack::class.java)

        val imageView = findViewById<ImageView>(R.id.track_image)
        currentTrack.imageUrl?.let { Picasso.get().load(it).into(imageView) }

        val nameTextView = findViewById<TextView>(R.id.track_name_tv)
        nameTextView.text = currentTrack.name
        nameTextView.ellipsize = TextUtils.TruncateAt.END

        val infoTextView = findViewById<TextView>(R.id.track_info_tv)
        infoTextView.text = resources.getString(R.string.track_info, if (currentTrack.artists.isNotEmpty()) currentTrack.artists[0] else "null", currentTrack.album)
        nameTextView.ellipsize = TextUtils.TruncateAt.END

        val tagManagerView = findViewById<CustomTagManagerForSingleTrackView>(R.id.single_track_tag_manager)
        tagManagerView.apply {
            track = currentTrack
            thread {
                // Initialize current tags
                val dbHelper = DBHelper(context)
                val currentTags = dbHelper.selectTagNamesBySongId(track.id)
                val allTags = dbHelper.selectAllTags()
                runOnUiThread {
                    tagList = currentTags
                    autoCompleteTagList = allTags
                }
                dbHelper.close()
            }
        }
    }
}