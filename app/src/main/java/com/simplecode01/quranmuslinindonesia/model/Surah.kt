package com.simplecode01.quranmuslinindonesia.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.PrimaryKey

@DatabaseView("SELECT id, sora, sora_name_en, sora_name_ar, count(id) AS ayat_total FROM quran GROUP by sora")
data class Surah(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "sora") val surahNumber: Int,
    @ColumnInfo(name = "sora_name_en") val surahName: String,
    @ColumnInfo(name = "sora_name_ar") val surahNameArabic: String,
    @ColumnInfo(name = "ayat_total") val ayahTotal: Int
)