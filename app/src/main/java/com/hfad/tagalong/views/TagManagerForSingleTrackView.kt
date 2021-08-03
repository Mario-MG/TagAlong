package com.hfad.tagalong.views

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.AttributeSet
import android.widget.Toast
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.DBHelper
import com.hfad.tagalong.tools.api.PlaylistManager
import com.hfad.tagalong.types.CustomTrack
import com.hfad.tagmanagerview.TagManagerView
import java.util.*
import kotlin.concurrent.thread

class TagManagerForSingleTrackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : TagManagerView(context, attrs, defStyleAttr, defStyleRes) {
    lateinit var track: CustomTrack

    override fun preProcessText() {
        inputText = inputText.toLowerCase(Locale.ROOT)
    }

    override fun addItemToList(item: String) {
        try { // TODO: thread?
            addSongWithTagToDb()
            addSongToPlaylists()
            super.addItemToList(item)
        } catch (ex: SQLiteConstraintException) {
            showRepeatedTagToast()
        }
    }

    private fun addSongWithTagToDb() {
        DBHelper(context).apply {
            insertSongWithTag(track, inputText)
            close()
        }
    }

    private fun addSongToPlaylists() {
        val dbHelper = DBHelper(context)
        val playlists = dbHelper.selectPlaylistsToAddSongWithTag(track.id, inputText)
        playlists.forEach { playlistId ->
            thread {
                PlaylistManager.addTracksToPlaylist(playlistId, track.id)
            }
        }
    }

    private fun showRepeatedTagToast() {
        if (context is Activity) {
            val activity = context as Activity
            activity.runOnUiThread {
                Toast.makeText(
                    context,
                    context.getString(R.string.repeatedTagName),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun postProcessText() {
        inputText = ""
    }

    override fun onClickCloseIcon(position: Int, tagName: String) {
        super.onClickCloseIcon(position, tagName)
        thread {
            DBHelper(context).apply {
                deleteSongWithTag(track.id, tagName)
                close()
            }
        }
    }
}