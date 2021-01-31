package com.hfad.tagalong.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.DBHelper
import com.hfad.tagalong.tools.api.PlaylistManager
import com.hfad.tagalong.views.CustomTagManagerForPlaylistCreationView
import kotlinx.android.synthetic.main.fragment_manager.*
import kotlin.concurrent.thread

class ManagerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.apply {
            val activity = this
            val tagManagerView = findViewById<CustomTagManagerForPlaylistCreationView>(R.id.tag_manager_for_playlist_creation)
            tagManagerView.apply {
                thread {
                    val dbHelper = DBHelper(activity)
                    // Initialize current tags
                    val allTags = dbHelper.selectAllTags()
                    activity.runOnUiThread {
                        autoCompleteTagList = allTags
                    }
                    dbHelper.close()
                }
            }
            val createPlaylistButton = findViewById<Button>(R.id.create_playlist_button)
            createPlaylistButton.setOnClickListener {
                val selectedTags = tagManagerView.tagList
                thread {
                    val spinner = findViewById<Spinner>(R.id.create_playlist_spinner)
                    val dbHelper = DBHelper(activity)
                    val songIds = when (spinner.selectedItemPosition) {
                        0 -> dbHelper.selectSongIdsWithAllTags(*selectedTags.toTypedArray())
                        1 -> dbHelper.selectSongIdsWithAnyTags(*selectedTags.toTypedArray())
                        else -> emptyList()
                    }
                    val playlistCreatedResponse = PlaylistManager.createPlaylist("TagAlong Playlist")
                    if (songIds.isNotEmpty() && playlistCreatedResponse?.success() == true) {
                        val playlistId = playlistCreatedResponse.result?.id!!
                        val songsAddedToPlaylistResponse = PlaylistManager.addTracksToPlaylist(
                            playlistId,
                            *songIds.toTypedArray()
                        )
                        if (songsAddedToPlaylistResponse?.statusCode == 200) {
                            activity.runOnUiThread {
                                Toast.makeText(
                                    activity,
                                    "Playlist created successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    dbHelper.close()
                }
            }
        }
    }
}