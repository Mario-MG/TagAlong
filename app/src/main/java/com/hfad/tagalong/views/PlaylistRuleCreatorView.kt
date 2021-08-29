package com.hfad.tagalong.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import com.hfad.tagalong.R
import com.hfad.tagalong.config.Optionality
import com.hfad.tagalong.tools.api.PlaylistManager
import com.hfad.tagalong.tools.db.room.RoomDbHelper
import com.hfad.tagalong.types.*
import com.hfad.tagmanagerview.TagManagerView
import kotlin.concurrent.thread

class PlaylistRuleCreatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes), RuleSubject {
    private val playlistNameEditText: EditText
    private val autoUpdateSwitch: SwitchCompat
    private val tagManager: TagManagerView
    private val optionalitySpinner: Spinner
    private val createPlaylistRuleButton: Button

    private lateinit var dbHelper: DbHelper
    private val subscribers: HashSet<RuleObserver> = HashSet()

    lateinit var rule: PlaylistCreationRule
        private set

    private var playlistName = ""
        get() = playlistNameEditText.text.toString()
        set(value) {
            field = value
            playlistNameEditText.setText(field)
        }

    private var autoUpdate = true
        get() = autoUpdateSwitch.isChecked
        set(value) {
            field = value
            autoUpdateSwitch.isChecked = field
        }

    private var tags: MutableList<String> = emptyList<String>().toMutableList()
        get() = tagManager.tagList
        set(value) {
            field = value
            tagManager.tagList = field
        }

    private var optionality = false
        get() = optionalitySpinner.selectedItemPosition == Optionality.ANY.ordinal
        set(value) {
            field = value
            optionalitySpinner.setSelection(Optionality.forValue(field).ordinal)
        }

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.view_playlist_rule_creator, this, true)

        playlistNameEditText = findViewById(R.id.playlist_name_edittext)
        autoUpdateSwitch = findViewById(R.id.autoupdate_switch)
        tagManager = findViewById(R.id.tag_manager_rule)
        optionalitySpinner = findViewById(R.id.create_playlist_spinner)
        createPlaylistRuleButton = findViewById(R.id.create_playlist_button)

        thread {
            dbHelper = RoomDbHelper(context)
            val allTags = dbHelper.getAllTags()
            (context as Activity).runOnUiThread {
                tagManager.autoCompleteTagList = allTags.map(Tag::name).toMutableList()
            }
        }

        createPlaylistRuleButton.setOnClickListener {
            onClickCreateRuleButton()
        }
    }

    private fun onClickCreateRuleButton() {
        thread {
            val playlistId = createPlaylistWithSelectedTags(tags)
            if (playlistId != null) {
                createRuleWithPlaylistId(playlistId)
            }
            notifyObservers(rule)
            resetFields()
        }
    }

    private fun createPlaylistWithSelectedTags(selectedTags: Collection<String>): String? {
        val songIds = getSongIdsForSelectedTagsFromDb(selectedTags)
        if (songIds.isNotEmpty()) {
            return createAndPopulatePlaylist(songIds)
        }
        return null
    }

    private fun getSongIdsForSelectedTagsFromDb(selectedTags: Collection<String>): List<String> {
        dbHelper = RoomDbHelper(context)
        val songIds = selectSongIdsWithSelectedTags(selectedTags).map(Track::id)
        return songIds
    }

    private fun selectSongIdsWithSelectedTags(selectedTags: Collection<String>): List<Track> {
        return if (optionality) {
            dbHelper.getSongsWithAnyOfTheTags(*selectedTags.map(::Tag).toTypedArray())
        } else {
            dbHelper.getSongsWithAllOfTheTags(*selectedTags.map(::Tag).toTypedArray())
        }
    }

    private fun createAndPopulatePlaylist(songIds: List<String>): String? {
        val createPlaylistResponse = PlaylistManager.createPlaylist(playlistName)
        if (createPlaylistResponse.success) {
            val playlistId = createPlaylistResponse.result!!.id
            return populatePlaylist(playlistId, songIds)
        }
        return null
    }

    private fun populatePlaylist(playlistId: String, songIds: List<String>): String? {
        val songsAddedToPlaylistResponse = PlaylistManager.addTracksToPlaylist(
            playlistId,
            *songIds.toTypedArray()
        )
        if (songsAddedToPlaylistResponse?.success == true) {
            return playlistId
        }
        return null
    }

    private fun createRuleWithPlaylistId(playlistId: String) {
        rule = PlaylistCreationRule(
            tags.map(::Tag),
            playlistId,
            optionality,
            autoUpdate
        )
        saveRuleToDB()
    }

    private fun saveRuleToDB() {
        dbHelper = RoomDbHelper(context)
        val ruleId = dbHelper.insertRule(rule)
        rule.ruleId = ruleId
    }

    private fun resetFields() {
        val activity = if (context is Activity) context as Activity else null
        activity?.runOnUiThread {
            playlistName = resources.getString(R.string.default_new_playlist_name)
            autoUpdate = true
            tags = ArrayList()
            optionality = false
        }
    }

    override fun subscribe(observer: RuleObserver) {
        subscribers.add(observer)
    }

    override fun unsubscribe(observer: RuleObserver) {
        subscribers.remove(observer)
    }

    override fun notifyObservers(rule: PlaylistCreationRule) {
        subscribers.forEach { observer ->
            observer.onCreateRule(rule)
        }
    }
}