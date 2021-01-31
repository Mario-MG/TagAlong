package com.hfad.tagalong.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.DBHelper
import com.hfad.tagalong.tools.adapters.TagsAdapter
import kotlin.concurrent.thread

class AllTagsFragment : Fragment() {
    private lateinit var tags: ArrayList<String>
    private lateinit var rvTags: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_playlists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTags = view.findViewById(R.id.rvPlaylists)
    }

    override fun onResume() {
        super.onResume()

        activity?.let {
            val dbHelper = DBHelper(it)
            thread {
                // Initialize playlists
                tags = dbHelper.selectAllTags()
                // Create Adapter passing in the playlists
                val adapter = TagsAdapter(tags)
                activity?.runOnUiThread {
                    // Attach the Adapter to the RecyclerView to populate items
                    rvTags.adapter = adapter
                    // Set layout manager to position the items
                    rvTags.layoutManager = LinearLayoutManager(activity)
                }
                dbHelper.close()
            }
        }
    }
}