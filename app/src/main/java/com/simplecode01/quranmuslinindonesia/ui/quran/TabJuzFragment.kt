package com.simplecode01.quranmuslinindonesia.ui.quran

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simplecode01.quranmuslinindonesia.R
import com.simplecode01.quranmuslinindonesia.data.QuranDatabase
import com.simplecode01.quranmuslinindonesia.databinding.FragmentTabJuzBinding

class TabJuzFragment: Fragment(R.layout.fragment_tab_juz) {
    private val binding: FragmentTabJuzBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manggilJuzDatabase()
    }
    private fun manggilJuzDatabase(){
        context?.let {
            val database : QuranDatabase = QuranDatabase.getInstance(it)
            val quranDao = database.quranDao()
            quranDao.getJuz().asLiveData().observe(viewLifecycleOwner, {juzList ->
                val adapter: QuranJuzAdapter = QuranJuzAdapter(juzList){juz ->

                    val bundle = bundleOf(ReadQuranFragment.KEY_JUZ_NUMBER to juz.juzNumber)
                    findNavController().navigate(R.id.action_nav_quran_to_nav_read_quran, bundle)
                }
                binding.recyclerview.adapter = adapter
            })
        }
    }
}