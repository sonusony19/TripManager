package com.trip.manager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.util.*

class PagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val fragments: ArrayList<Fragment> = ArrayList()
    private val titles: ArrayList<String> = ArrayList()
    override fun getCount() = fragments.size
    override fun getItem(i: Int) = fragments[i]
    override fun getPageTitle(position: Int) = titles[position]

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }

    /*  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
         // super.destroyItem(container, position, `object`)
      }*/
}