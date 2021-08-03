package com.hfad.tagalong.tools.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.api.PlaylistManager
import com.hfad.tagalong.types.PlaylistCreationRule
import com.hfad.tagalong.types.RuleObserver
import com.hfad.tagalong.views.PlaylistRuleCreatorView
import com.hfad.tagmanagerview.TagManagerView
import kotlin.concurrent.thread

class PlaylistCreatorAdapter(
    private val activity: FragmentActivity,
    private val playlistCreationRules: ArrayList<PlaylistCreationRule>
) : RecyclerView.Adapter<PlaylistCreatorAdapter.ViewHolder>(), RuleObserver {
    private lateinit var creatorView: PlaylistRuleCreatorView

    companion object {
        private const val RULE_CREATOR_VIEW_TYPE = 1
    }

    inner class ViewHolder(listItemView: View, viewType: Int) : RecyclerView.ViewHolder(listItemView) {
        var nameTextView: TextView? = null
        var autoUpdateSwitch: SwitchCompat? = null
        var tagManager: TagManagerView? = null
        var optionalityTextView: TextView? = null

        private val context = listItemView.context

        private lateinit var rule: PlaylistCreationRule

        init {
            if (viewType != RULE_CREATOR_VIEW_TYPE) {
                nameTextView = itemView.findViewById(R.id.playlist_name_textview)
                autoUpdateSwitch = itemView.findViewById(R.id.autoupdate_switch)
                tagManager = itemView.findViewById(R.id.tag_manager_rule)
                optionalityTextView = itemView.findViewById(R.id.optionality_textview)
            }
        }

        fun bindRule(rule: PlaylistCreationRule) {
            this.rule = rule
            populate()
        }

        private fun populate() {
            populateNameTextView()
            setAutoUpdateSwitchChecked()
            populateTagManager()
            populateOptionalityTextView()
        }

        private fun populateNameTextView() {
            thread {
                val playlistResponse = PlaylistManager.getPlaylist(rule.playlistId)
                val playlistName = playlistResponse.result?.name
                (context as Activity).runOnUiThread {
                    nameTextView!!.text = playlistName
                }
            }
        }

        private fun setAutoUpdateSwitchChecked() {
            autoUpdateSwitch!!.isChecked = rule.autoUpdate
        }

        private fun populateTagManager() {
            tagManager!!.tagList = rule.tags
        }

        private fun populateOptionalityTextView() {
            optionalityTextView!!.text = rule.optionality.name
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position >= playlistCreationRules.size) {
            return 1
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistCreatorAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemView = if (viewType == RULE_CREATOR_VIEW_TYPE) {
            inflater.inflate(R.layout.item_playlist_creator, parent, false)
        } else {
            inflater.inflate(R.layout.item_playlist_creation_rule, parent, false)
        }
        return ViewHolder(itemView, viewType)
    }

    override fun onBindViewHolder(viewHolder: PlaylistCreatorAdapter.ViewHolder, position: Int) {
        if (position < playlistCreationRules.size) {
            viewHolder.bindRule(playlistCreationRules[position])
        } else {
            creatorView = viewHolder.itemView.findViewById(R.id.playlist_rule_creator)
            creatorView.subscribe(this)
        }
    }

    override fun getItemCount(): Int {
        return playlistCreationRules.size + 1
    }

    override fun onCreateRule(rule: PlaylistCreationRule) {
        activity.runOnUiThread {
            playlistCreationRules.add(rule)
            notifyItemInserted(playlistCreationRules.size - 1)
        }
    }
}