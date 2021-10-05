package com.simplecode01.quranmuslinindonesia.data

import com.simplecode01.quranmuslinindonesia.model.Quran
import com.simplecode01.quranmuslinindonesia.model.Page
import kotlinx.coroutines.flow.Flow

class QuranRepository(val quranDatabase: QuranDatabase) {

    fun getQuran(): Flow<List<Quran>> {
        return quranDatabase.quranDao().getQuran()
    }

    // getSurah
    // getAyah

}