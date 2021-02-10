package com.hfad.tagalong.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.DBHelper
import com.hfad.tagalong.tools.api.PlaylistManager
import com.hfad.tagalong.views.CustomTagManagerForPlaylistCreationView
import kotlin.concurrent.thread

class ManagerFragment : Fragment() {
    private lateinit var mActivity: FragmentActivity
    private lateinit var mDbHelper: DBHelper

    private lateinit var tagManagerView: CustomTagManagerForPlaylistCreationView
    private lateinit var createPlaylistButton: Button
    private lateinit var spinner: Spinner

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFragment()
    }

    private fun initializeFragment() {
        initializeTagManagerView()
        initializeSpinner()
        initializeCreatePlaylistButton()
    }

    private fun initializeTagManagerView() {
        tagManagerView = mActivity.findViewById(R.id.tag_manager_for_playlist_creation)
        thread {
            initializeAutoCompleteTagList()
        }
    }

    private fun initializeAutoCompleteTagList() {
        val allTags = getAllTagsFromDb()
        mActivity.runOnUiThread {
            tagManagerView.autoCompleteTagList = allTags
        }
    }

    private fun getAllTagsFromDb(): ArrayList<String> {
        mDbHelper = DBHelper(mActivity)
        val allTags = mDbHelper.selectAllTags()
        mDbHelper.close()
        return allTags
    }

    private fun initializeSpinner() {
        spinner = mActivity.findViewById(R.id.create_playlist_spinner)
        val adapter = ArrayAdapter.createFromResource(
            mActivity,
            R.array.create_playlist_spinner_entries,
            R.layout.spinner_item
        )
        spinner.adapter = adapter
    }

    private fun initializeCreatePlaylistButton() {
        createPlaylistButton = mActivity.findViewById(R.id.create_playlist_button)
        createPlaylistButton.setOnClickListener {
            onClickCreatePlaylistButton()
        }
    }

    private fun onClickCreatePlaylistButton() {
        val selectedTags = tagManagerView.tagList
        thread {
            createPlaylistWithSelectedTags(selectedTags)
        }
    }

    private fun createPlaylistWithSelectedTags(selectedTags: ArrayList<String>) {
        val songIds = getSongIdsForSelectedTagsFromDb(selectedTags)
        if (songIds.isNotEmpty()) {
            createAndPopulatePlaylist(songIds)
        }
    }

    private fun getSongIdsForSelectedTagsFromDb(selectedTags: ArrayList<String>): List<String> {
        mDbHelper = DBHelper(mActivity)
        val songIds = selectSongIdsWithSelectedTags(selectedTags)
        mDbHelper.close()
        return songIds
    }

    private fun createAndPopulatePlaylist(songIds: List<String>) {
        val playlistCreatedResponse = PlaylistManager.createPlaylist("TagAlong Playlist")
        if (playlistCreatedResponse?.success == true) {
            val playlistId = playlistCreatedResponse.result!!.id
            populatePlaylistAndShowToast(playlistId, songIds)
        }
    }

    private fun selectSongIdsWithSelectedTags(selectedTags: ArrayList<String>): List<String> {
        return when (spinner.selectedItemPosition) {
            0 -> mDbHelper.selectSongIdsWithAllTags(*selectedTags.toTypedArray())
            1 -> mDbHelper.selectSongIdsWithAnyTags(*selectedTags.toTypedArray())
            else -> emptyList()
        }
    }

    private fun populatePlaylistAndShowToast(
        playlistId: String,
        songIds: List<String>
    ) {
        val songsAddedToPlaylistResponse = PlaylistManager.addTracksToPlaylist(
            playlistId,
            *songIds.toTypedArray()
        )
        if (songsAddedToPlaylistResponse?.success == true) {
            showSuccessToast()
        }
    }

    private fun showSuccessToast() {
        mActivity.runOnUiThread {
            Toast.makeText(
                mActivity,
                "Playlist created successfully",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}