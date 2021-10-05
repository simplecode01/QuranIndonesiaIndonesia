package com.simplecode01.quranmuslinindonesia.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran")
data class Quran(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo(name = "jozz") val juzNumber: Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber: Int? = 0,
    @ColumnInfo(name = "sora_name_en") val surahName: String? = "",
    @ColumnInfo(name = "sora_name_ar") val surahNameArabic: String? ="",
    @ColumnInfo(name = "sora_latin") val surahLatin: String? = "",
    @ColumnInfo(name = "sora_asal") val surahAsal: String? = "",
    @ColumnInfo(name = "sora_latin_read") val surahLatinRead: String? = "",

    @ColumnInfo(name = "page") val pageNumber: Int? = 0,
    @ColumnInfo(name = "line_start") val lineStart: Int? = 0,
    @ColumnInfo(name = "line_end") val lineEnd: Int? = 0,
    @ColumnInfo(name = "aya_no") val ayahNumber: Int? = 0,
    @ColumnInfo(name = "aya_text") val textQuran: String? = "",
    @ColumnInfo(name = "aya_text_emlaey") val textQuranSearch: String? = "",
    val translation: String? = "",
    val footnotes: String? = "",
)