package com.hfad.tagalong.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.DBHelper
import com.hfad.tagmanagerview.TagManagerView
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

        val manager = activity?.findViewById<TagManagerView>(R.id.prueba)
        manager?.apply {
            setTagList(arrayListOf("Quizá"))
            setOnClickTagCloseIcon { _, tagName ->
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
                    val dbHelper = DBHelper(activity)
                    val songIds = dbHelper.selectSongIdsWithAllTags(*selectedTags.toTypedArray())
                    val playlistCreatedResponse = PlaylistManager.createPlaylist("TagAlong Playlist")
                    if (songIds?.isNotEmpty() == true && playlistCreatedResponse?.success() == true) {
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
            ready()
        }
    }
}