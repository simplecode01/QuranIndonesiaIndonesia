package com.simplecode01.quranmuslinindonesia.ui.quran

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.simplecode01.quranmuslinindonesia.data.QuranRepository
import com.simplecode01.quranmuslinindonesia.model.Quran
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModelProvider
import com.simplecode01.quranmuslinindonesia.model.Surah
import java.text.FieldPosition

class QuranViewModel: ViewModel() {
    private val tabPosition: MutableLiveData<Int> = MutableLiveData()
    private val totalAyahList: MutableLiveData<List<Int>> = MutableLiveData()

    fun setTabPosisition(position: Int){
        tabPosition.value = position
    }
    fun getTabPosisition() = tabPosition

    fun setTotalAyahList(surahList: List<Surah>){
        val totalList = mutableListOf<Int>()
        surahList.forEach{surah ->
            val total = surah.ayahTotal?: 1
            totalList.add(total)
        }
        totalAyahList.value = totalList
    }

    fun getTotalAyahList() = totalAyahList
}