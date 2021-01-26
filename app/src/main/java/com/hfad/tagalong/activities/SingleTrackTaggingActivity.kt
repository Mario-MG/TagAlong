package com.hfad.tagalong.activities

import android.database.sqlite.SQLiteConstraintException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.hfad.tagalong.R
import com.hfad.tagalong.config.Extras
import com.hfad.tagalong.tools.DBHelper
import com.hfad.tagalong.tools.adapters.TagViewAdapter
import com.hfad.tagalong.types.CustomTrack
import com.hfad.tagmanagerview.TagManagerView
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.concurrent.thread

class SingleTrackTaggingActivity : AppCompatActivity() {
    private lateinit var track: CustomTrack
    private lateinit var adapter: TagViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_track_tagging)

        val trackData = intent.getStringExtra(Extras.TRACK_DATA)
        track = Gson().fromJson(trackData, CustomTrack::class.java)

        val imageView = findViewById<ImageView>(R.id.track_image)
        track.imageUrl?.let { Picasso.get().load(it).into(imageView) }

        val nameTextView = findViewById<TextView>(R.id.track_name_tv)
        nameTextView.text = track.name
        nameTextView.ellipsize = TextUtils.TruncateAt.END

        val infoTextView = findViewById<TextView>(R.id.track_info_tv)
        infoTextView.text = resources.getString(R.string.track_info, if (track.artists.isNotEmpty()) track.artists[0] else "null", track.album)
        nameTextView.ellipsize = TextUtils.TruncateAt.END

        val tagManagerView = findViewById<TagManagerView>(R.id.single_track_tag_manager)
//        tagManagerView.apply {
//            val dbHelper = DBHelper(context)
//            thread {
//                // Initialize playlists
//                val currentTags = dbHelper.selectAllTags()
//                runOnUiThread {
//                    setTagList(currentTags)
//                    setOnClickTagCloseIcon { _, tagName ->
//                        thread {
//                            dbHelper.deleteSongWithTag(track.id, tagName)
//                        }
//                    }
//                    setOnAddTagButtonClick {
//                        val tagName = getText().toLowerCase()
//                        thread {
//                            try {
//                                dbHelper.insertSongWithTag(track, tagName)
//                                setText("")
//                            } catch (ex: SQLiteConstraintException) {
//                                runOnUiThread {
//                                    Toast.makeText(
//                                        context,
//                                        getString(R.string.repeatedTagName),
//                                        Toast.LENGTH_LONG
//                                    ).show()
//                                }
//                            }
//                        }
//                    }
//                    ready()
//                }
//            }
//        }
    }
}