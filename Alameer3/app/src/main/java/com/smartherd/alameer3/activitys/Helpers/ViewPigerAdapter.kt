package com.smartherd.alameer3.activitys.Helpers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPigerAdapter(Frma: FragmentManager) : FragmentPagerAdapter(Frma) {
    var fragment =ArrayList<Fragment>()
    var tapTital = ArrayList<String>()

    fun addFragment(fragment: Fragment,tabTital:String){
        this.fragment.add(fragment)
        this.tapTital.add(tabTital)
    }
    override fun getCount(): Int {
        return this.tapTital.size
    }

    override fun getItem(position: Int): Fragment {
        return this.fragment[position]
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return this.tapTital[position]
    }
}