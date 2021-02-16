package com.hfad.tagalong.tools.adapters

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hfad.tagalong.R
import com.hfad.tagalong.activities.AllPlaylistsFragment
import com.hfad.tagalong.activities.AllTagsFragment
import com.hfad.tagalong.activities.ManagerFragment

// Sources:
// https://www.geeksforgeeks.org/how-to-implement-a-tablayout-in-android-using-viewpager-and-fragments/
// https://medium.com/@pjonceski/fragmentpageradapter-with-fragments-that-restore-their-state-properly-a427ecfd792e
class MainFragmentPagerAdapter(
    fm: FragmentManager,
    private val context: Context
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragments = SparseArray<Fragment>()

    enum class Tab(val titleResourceId: Int) {
        PLAYLISTS(R.string.playlists_tab_name),
        TAGS(R.string.tags_tab_name),
        MANAGER(R.string.manager_tab_name);

        companion object {
            fun getTabByPosition(position: Int): Tab? {
                return values().find { tab -> tab.ordinal == position }
            }
        }
    }

    override fun getCount(): Int {
        return Tab.values().size
    }

    override fun getItem(position: Int): Fragment {
        val fragment = getFragment(position)
        return fragment ?: when (position) {
            Tab.PLAYLISTS.ordinal -> AllPlaylistsFragment()
            Tab.TAGS.ordinal -> AllTagsFragment()
            Tab.MANAGER.ordinal -> ManagerFragment()
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

    private fun getFragment(position: Int): Fragment? {
        return if (mFragments.size() == 0) null else mFragments.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence {
        val tab = Tab.getTabByPosition(position)!!
        val tabTitle = context.resources.getString(tab.titleResourceId)
        return tabTitle
    }
}