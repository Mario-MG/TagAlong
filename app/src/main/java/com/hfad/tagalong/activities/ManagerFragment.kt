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

//        val manager = activity?.findViewById<TagManagerView>(R.id.prueba)
//        manager?.apply {
//            setTagList(arrayListOf("Quizá"))
//            setOnClickTagCloseIcon { _, tagName ->
//                thread {
//                    val dbHelper = DBHelper(activity!!)
//                }
//            }
//            ready()
//        }
    }
}