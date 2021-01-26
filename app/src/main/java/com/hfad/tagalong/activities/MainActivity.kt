package com.hfad.tagalong.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hfad.tagalong.R
import com.hfad.tagalong.tools.adapters.MainFragmentPagerAdapter

// Source: https://www.geeksforgeeks.org/how-to-implement-a-tablayout-in-android-using-viewpager-and-fragments/
class MainActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: MainFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        val tabTitles = arrayOf(
            getString(R.string.playlists_tab_name),
            getString(R.string.tags_tab_name),
            getString(R.string.manager_tab_name)
        )
        viewPagerAdapter = MainFragmentPagerAdapter(supportFragmentManager, tabTitles)
        viewPager.adapter = viewPagerAdapter

        tabLayout.setupWithViewPager(viewPager);
    }
}