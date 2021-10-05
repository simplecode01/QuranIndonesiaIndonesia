package com.simplecode01.quranmuslinindonesia.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.PrimaryKey

@DatabaseView("SELECT MIN(id) AS id, jozz, aya_no, aya_text, sora_name_en from quran GROUP by jozz ORDER BY id")
data class Juz(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "jozz") val juzNumber: Int,
    @ColumnInfo(name = "aya_no") val ayahNumber: Int,
    @ColumnInfo(name = "sora_name_en") val surahName: String,
    @ColumnInfo(name = "aya_text") val textQuran: String
)