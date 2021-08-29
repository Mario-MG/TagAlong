package com.hfad.tagalong.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.adapters.TagsAdapter
import com.hfad.tagalong.tools.db.room.RoomDbHelper
import com.hfad.tagalong.types.Tag
import kotlin.concurrent.thread

class AllTagsFragment : Fragment() {
    private lateinit var mActivity: FragmentActivity

    private lateinit var tags: List<String>
    private lateinit var tagsRecyclerView: RecyclerView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_playlists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeTagsRecyclerView(view)
    }

    private fun initializeTagsRecyclerView(view: View) {
        tagsRecyclerView = view.findViewById(R.id.playlists_recyclerview)
    }

    override fun onResume() {
        super.onResume()
        populateTagsRecyclerView()
    }

    private fun populateTagsRecyclerView() {
        thread {
            getAllTagsFromDb()
            setTagsIntoRecyclerView()
        }
    }

    private fun getAllTagsFromDb() {
        val dbHelper = RoomDbHelper(mActivity)
        tags = dbHelper.getAllTags().map(Tag::name)
    }

    private fun setTagsIntoRecyclerView() {
        val adapter = TagsAdapter(tags)
        mActivity.runOnUiThread {
            tagsRecyclerView.adapter = adapter
            tagsRecyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }
}