package com.hfad.tagalong.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.adapters.MainFragmentPagerAdapter
import com.hfad.tagalong.tools.api.TokenManager

// Source: https://www.geeksforgeeks.org/how-to-implement-a-tablayout-in-android-using-viewpager-and-fragments/
class MainActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: MainFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (TokenManager.isUserNotLoggedIn()) {
            startLoginActivity()
        }

        initializeUI()
    }

    private fun startLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(loginIntent)
    }

    private fun initializeUI() {
        initializeViewPager()
        initializeTabLayout()
    }

    private fun initializeViewPager() {
        viewPager = findViewById(R.id.view_pager)
        val tabTitles = arrayOf(
            getString(R.string.playlists_tab_name),
            getString(R.string.tags_tab_name),
            getString(R.string.manager_tab_name)
        )
        viewPagerAdapter = MainFragmentPagerAdapter(supportFragmentManager, tabTitles)
        viewPager.adapter = viewPagerAdapter
    }

    private fun initializeTabLayout() {
        tabLayout = findViewById(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager)
    }
}