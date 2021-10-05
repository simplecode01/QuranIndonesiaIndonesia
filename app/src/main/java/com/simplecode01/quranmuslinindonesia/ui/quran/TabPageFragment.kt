package com.simplecode01.quranmuslinindonesia.ui.quran

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simplecode01.quranmuslinindonesia.R
import com.simplecode01.quranmuslinindonesia.data.QuranDatabase
import com.simplecode01.quranmuslinindonesia.databinding.FragmentTabPageBinding

class TabPageFragment: Fragment(R.layout.fragment_tab_page) {
    private val binding: FragmentTabPageBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manggilPageDatabase()
    }
    private fun manggilPageDatabase(){
        context?.let {
            val database : QuranDatabase = QuranDatabase.getInstance(it)
            val quranDao = database.quranDao()
            quranDao.getPage().asLiveData().observe(viewLifecycleOwner, {pageList ->
                val adapter: QuranPageAdapter = QuranPageAdapter(pageList) {page ->

                    val bundle = bundleOf(ReadQuranFragment.KEY_PAGE_NUMBER to page.pageNumber)
                    findNavController().navigate(R.id.action_nav_quran_to_nav_read_quran, bundle)
                }
                binding.recyclerview.adapter = adapter
            })
        }
    }
}