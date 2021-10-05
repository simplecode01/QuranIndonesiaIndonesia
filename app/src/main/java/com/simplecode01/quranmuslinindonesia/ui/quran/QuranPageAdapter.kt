package com.simplecode01.quranmuslinindonesia.ui.quran

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simplecode01.quranmuslinindonesia.R
import com.simplecode01.quranmuslinindonesia.databinding.ItemPageBinding
import com.simplecode01.quranmuslinindonesia.model.Page

class QuranPageAdapter(val listQuran: List<Page>, val itemClickListener: ((page: Page)->Unit))
    : RecyclerView.Adapter<QuranPageAdapter.QuranPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranPageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        return QuranPageViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuranPageViewHolder, position: Int) {
        val page = listQuran[position]
        holder.bindView(page)
        holder.itemView.setOnClickListener{
            itemClickListener.invoke(page)
        }
    }

    override fun getItemCount(): Int {
        return listQuran.size
    }

    class QuranPageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: ItemPageBinding by viewBinding()

        fun bindView(page: Page) {
            binding.textPageName.text = "Page ${page.pageNumber}"
            binding.textPageNumber.text = page.pageNumber.toString()
            binding.textQuranAyah.text = page.textQuran
            binding.textSurahWithNumber.text = "${page.surahName}: ${page.ayahNumber}"
        }

    }
}