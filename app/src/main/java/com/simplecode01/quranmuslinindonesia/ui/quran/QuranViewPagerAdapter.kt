package com.simplecode01.quranmuslinindonesia.ui.quran

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class QuranViewPagerAdapter(val fragment: Fragment,
                            val fragmenList: List<Fragment>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return fragmenList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmenList[position]
    }
}