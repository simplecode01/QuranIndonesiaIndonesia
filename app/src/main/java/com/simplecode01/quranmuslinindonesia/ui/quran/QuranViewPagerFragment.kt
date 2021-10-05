package com.simplecode01.quranmuslinindonesia.ui.quran

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.simplecode01.quranmuslinindonesia.R
import com.simplecode01.quranmuslinindonesia.databinding.FragmentAboutUsBinding
import com.simplecode01.quranmuslinindonesia.databinding.FragmentQuranBinding
import com.simplecode01.quranmuslinindonesia.ui.about.AboutUsFragment
import com.simplecode01.quranmuslinindonesia.ui.rating.RatingFragment
import com.simplecode01.quranmuslinindonesia.ui.contact.ContactFragment

class QuranViewPagerFragment: Fragment(R.layout.fragment_quran) {
    private val binding: FragmentQuranBinding by viewBinding()
    private val tittle = arrayOf("Surah", "Juz", "Page")
    private val fragmentList = listOf(TabSurahFragment(), TabJuzFragment(), TabPageFragment())
    private val viewModel: QuranViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = QuranViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setTabPosisition(position)
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = tittle[position]
        }.attach()
    }
}