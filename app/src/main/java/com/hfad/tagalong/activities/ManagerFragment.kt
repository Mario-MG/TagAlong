package com.hfad.tagalong.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.db.SqliteDbHelper
import com.hfad.tagalong.tools.adapters.PlaylistCreatorAdapter
import com.hfad.tagalong.types.PlaylistCreationRule
import com.hfad.tagalong.views.TagManagerForPlaylistCreationView
import kotlin.concurrent.thread

class ManagerFragment : Fragment() {
    private lateinit var mActivity: FragmentActivity
    private lateinit var mDbHelper: SqliteDbHelper

    private lateinit var recyclerView: RecyclerView
    private lateinit var tagManagerView: TagManagerForPlaylistCreationView
    private lateinit var createPlaylistButton: Button
    private lateinit var spinner: Spinner

    private lateinit var rules: ArrayList<PlaylistCreationRule>

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
        initializeRecyclerView()
        populateRecyclerView()
    }

    private fun initializeRecyclerView() {
        recyclerView = mActivity.findViewById(R.id.playlist_creator_recyclerview)
    }

    private fun populateRecyclerView() {
        thread {
            mDbHelper = SqliteDbHelper(mActivity)
            rules = mDbHelper.selectAllRulesWithTags()
            mDbHelper.close()
            setRulesIntoRecyclerView()
        }
    }

    private fun setRulesIntoRecyclerView() {
        val adapter = PlaylistCreatorAdapter(mActivity, rules)
        mActivity.runOnUiThread {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }
}