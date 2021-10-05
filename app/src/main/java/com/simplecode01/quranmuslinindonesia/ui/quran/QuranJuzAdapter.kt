package com.simplecode01.quranmuslinindonesia.ui.quran

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simplecode01.quranmuslinindonesia.R
import com.simplecode01.quranmuslinindonesia.databinding.ItemJuzBinding
import com.simplecode01.quranmuslinindonesia.model.Juz

class QuranJuzAdapter(val listQuran: List<Juz>, val itemClickListener: ((juz: Juz)->Unit))
    : RecyclerView.Adapter<QuranJuzAdapter.QuranJuzViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranJuzViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_juz, parent, false)
        return QuranJuzViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranJuzViewHolder, position: Int) {
        val juz = listQuran[position]
        holder.bindView(juz)
        holder.itemView.setOnClickListener{
            itemClickListener.invoke(juz)
        }
    }

    override fun getItemCount(): Int {
        return listQuran.size
    }

    class QuranJuzViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: ItemJuzBinding by viewBinding()

        fun bindView(juz: Juz) {
            binding.textJuzName.text = "Juz ${juz.juzNumber}"
            binding.textJuzNumber.text = juz.juzNumber.toString()
            binding.textSurahAwal.text = juz.textQuran
            binding.textSurahWithNumber.text = "${juz.surahName}: ${juz.ayahNumber}"

        }

    }
}