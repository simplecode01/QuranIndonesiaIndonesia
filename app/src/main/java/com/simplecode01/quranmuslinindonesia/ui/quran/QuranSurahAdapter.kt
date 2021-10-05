package com.simplecode01.quranmuslinindonesia.ui.quran

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simplecode01.quranmuslinindonesia.R
import com.simplecode01.quranmuslinindonesia.databinding.ItemSurahBinding
import com.simplecode01.quranmuslinindonesia.model.Surah

class QuranSurahAdapter(val listQuran: List<Surah>, val itemClickListener: ((surah: Surah)->Unit))
    : RecyclerView.Adapter<QuranSurahAdapter.QuranSurahViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranSurahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_surah, parent, false)
        return QuranSurahViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranSurahViewHolder, position: Int) {
        val surah = listQuran[position]
        holder.bindView(surah)
        holder.itemView.setOnClickListener{
            itemClickListener.invoke(surah)
        }
    }

    override fun getItemCount(): Int {
        return listQuran.size
    }

    class QuranSurahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: ItemSurahBinding by viewBinding()

        fun bindView(surah: Surah) {
            binding.textSurahNumber.text = surah.surahNumber.toString()
            binding.textSurahName.text = surah.surahName
            binding.textSurahNameAr.text = surah.surahNameArabic
            binding.textNumberOfAyah.text = "${surah.ayahTotal} ayat"

        }
    }
}