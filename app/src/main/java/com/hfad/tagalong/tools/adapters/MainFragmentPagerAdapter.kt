package com.hfad.tagalong.tools.adapters

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hfad.tagalong.activities.AllPlaylistsFragment
import com.hfad.tagalong.activities.AllTagsFragment
import com.hfad.tagalong.activities.ManagerFragment

// Sources:
// https://www.geeksforgeeks.org/how-to-implement-a-tablayout-in-android-using-viewpager-and-fragments/
// https://medium.com/@pjonceski/fragmentpageradapter-with-fragments-that-restore-their-state-properly-a427ecfd792e
class MainFragmentPagerAdapter(
    fm: FragmentManager,
    private val tabTitles: Array<String>
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    companion object {
        private const val NUMBER_OF_FRAGMENTS = 3
        private const val TAB_PLAYLISTS = 0
        private const val TAB_TAGS = 1
        private const val TAB_MANAGER = 2
    }

    private val mFragments = SparseArray<Fragment>()

    override fun getCount(): Int {
        return NUMBER_OF_FRAGMENTS
    }

    override fun getItem(position: Int): Fragment {
        val fragment = getFragment(position)
        return fragment ?: when (position) {
            TAB_PLAYLISTS -> AllPlaylistsFragment()
            TAB_TAGS -> AllTagsFragment()
            TAB_MANAGER -> ManagerFragment()
            else -> Fragment()
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        mFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        mFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getFragment(position: Int): Fragment? {
        return if (mFragments.size() == 0) null else mFragments.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitles[position]
    }
}