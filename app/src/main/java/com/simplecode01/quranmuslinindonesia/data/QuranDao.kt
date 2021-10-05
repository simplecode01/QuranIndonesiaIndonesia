package com.simplecode01.quranmuslinindonesia.data

import androidx.room.Dao
import androidx.room.Query
import com.simplecode01.quranmuslinindonesia.model.Juz
import com.simplecode01.quranmuslinindonesia.model.Page
import com.simplecode01.quranmuslinindonesia.model.Quran
import com.simplecode01.quranmuslinindonesia.model.Surah
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {
    @Query("SELECT * FROM quran")
    fun getQuran(): Flow<List<Quran>>

    @Query("SELECT * FROM page")
    fun getPage(): Flow<List<Page>>

    @Query("SELECT * FROM surah")
    fun getSurah(): Flow<List<Surah>>

    @Query("SELECT * FROM juz")
    fun getJuz(): Flow<List<Juz>>

    @Query("SELECT id, sora,aya_no ,sora_latin_read, sora_name_en, sora_latin ,sora_asal ,sora_name_ar, page,aya_text, aya_text_emlaey, jozz ,translation , footnotes FROM quran WHERE sora = :surahNumber")
    fun getSurahByNumber(surahNumber: Int): Flow<List<Quran>>

    @Query("SELECT id, sora,aya_no  ,sora_latin_read, sora_name_en, sora_latin ,sora_asal ,sora_name_ar, page,aya_text, aya_text_emlaey, jozz ,translation , footnotes FROM quran WHERE jozz = :juzNumber")
    fun getJuzByNumber(juzNumber: Int): Flow<List<Quran>>

    @Query("SELECT id, sora,aya_no ,sora_latin_read, sora_name_en, sora_name_ar, page,aya_text, aya_text_emlaey, jozz ,translation , footnotes FROM quran WHERE page = :pageNumber")
    fun getPageByNumber(pageNumber: Int): Flow<List<Quran>>
}