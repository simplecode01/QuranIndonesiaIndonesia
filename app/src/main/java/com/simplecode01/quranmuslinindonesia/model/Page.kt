package com.simplecode01.quranmuslinindonesia.model

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.PrimaryKey

@DatabaseView("SELECT MIN(id) AS id, page, sora_name_en, aya_no, aya_text FROM quran GROUP by page ORDER BY id")
data class Page(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "page") val pageNumber: Int,
    @ColumnInfo(name = "sora_name_en") val surahName: String,
    @ColumnInfo(name = "aya_no") val ayahNumber: Int,
    @ColumnInfo(name = "aya_text") val textQuran: String
)
