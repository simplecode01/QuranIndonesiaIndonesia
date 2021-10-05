package com.simplecode01.quranmuslinindonesia.ui.quran

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.color.MaterialColors
import com.simplecode01.quranmuslinindonesia.R
import com.simplecode01.quranmuslinindonesia.databinding.ItemAyahBinding
import com.simplecode01.quranmuslinindonesia.model.Quran
import java.util.regex.Matcher
import java.util.regex.Pattern

class ReadQuranAdapter(val listQuran: List<Quran>, val totalAyahList: List<Int>)
    : RecyclerView.Adapter<ReadQuranAdapter.ReadQuranViewHolder>() {

    var moreCLickListener: ((Quran, Int)->Unit)? = null

    var nextJuzNumber: Int = 0
    var nextPageNumber: Int = 0

    var footnotesOnClickListener: ((Quran)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadQuranViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ayah, parent, false)
        return ReadQuranViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReadQuranViewHolder, position: Int) {
        val quran = listQuran[position]
        val lastIndex = listQuran.lastIndex == position

        val index = if(lastIndex){
            position
        }else{
            position + 1
        }
        val nextData = listQuran[index]

        val surahNumber = quran.surahNumber ?: 1
        val totalAyah = totalAyahList[surahNumber - 1]


        holder.binding.textJuzOrPageNumber.isVisible = quran.pageNumber != nextPageNumber || quran.juzNumber != nextPageNumber

        holder.bindView(quran, position)

        holder.createSpannableFootontes(holder.itemView, quran, footnotesOnClickListener)

        holder.binding.textNumberOfAyah.text = "( $totalAyah Ayat )"

        holder.binding.imageMore.setOnClickListener{
            moreCLickListener?.invoke(quran, totalAyah)
        }

        nextJuzNumber = nextData.juzNumber?: 0
        nextPageNumber = nextData.pageNumber?: 0
    }


    override fun getItemCount(): Int {
        return listQuran.size
    }

    class ReadQuranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val binding: ItemAyahBinding by viewBinding()

        val indexSurahAwalNumber: Int = 0

        fun bindView(quran: Quran, position: Int) {
            binding.textSurahName.text =  "Qs. ${quran.surahName}"
            binding.textSurahNameAr.text = quran.surahNameArabic
            binding.textAyah.text = reverseAyahfromNumber(quran.textQuran!!)
            binding.textJuzOrPageNumber.text = "• Juz ${quran.juzNumber} • Hal ${quran.pageNumber} •"
            binding.textAyahNumber.text = quran.ayahNumber.toString()
            binding.textTranslation.text = quran.translation
            binding.textSurahLatin.text = quran.surahLatin
            binding.textSurahAsal.text = quran.surahAsal
            binding.textLatinRead.text = quran.surahLatinRead




            if(position == indexSurahAwalNumber){
                binding.containerSurahLayout.visibility = View.VISIBLE
            }else{
                binding.containerSurahLayout.visibility = View.GONE
            }

            if(quran.surahNumber == 1 || quran.surahNumber == 9){
                binding.textBismillah.visibility = View.GONE
            }else{
                binding.textBismillah.visibility = View.VISIBLE
            }

            if(quran.ayahNumber == 1 ){
                binding.textBismillah.visibility = View.VISIBLE
            }else{
                binding.textBismillah.visibility = View.GONE
            }

            if(quran.ayahNumber == 1){
                binding.textJuzOrPageNumber.visibility = View.VISIBLE
            }else{
                binding.textJuzOrPageNumber.visibility = View.GONE
            }

            if(quran.ayahNumber == 1 ){
                binding.containerSurahLayout.visibility = View.VISIBLE
            }else{
                binding.containerSurahLayout.visibility = View.GONE
            }

            if(quran.surahAsal == "Mekkah"){
                binding.imageMeccan.visibility = View.VISIBLE
                binding.imageMedian.visibility = View.GONE
            }else{
                binding.imageMeccan.visibility = View.GONE
                binding.imageMedian.visibility = View.VISIBLE
            }
        }

        fun createSpannableFootontes(view: View, quran: Quran, footnotesOnClickListener: ((Quran)->Unit)?){
            val colorPrimary = MaterialColors.getColor(view, R.attr.colorPrimary)
            val translation = quran.translation
            val sb = SpannableStringBuilder(translation)
            val p: Pattern = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE)
            val m: Matcher = p.matcher(translation)
            while (m.find()){
                val clickableSpan = object : ClickableSpan(){
                    override fun updateDrawState(textPaint: TextPaint) {
                        textPaint.color = colorPrimary
                        textPaint.isUnderlineText = true
                    }

                    override fun onClick(widget: View) {
                        footnotesOnClickListener?.invoke(quran)
                    }
                }
                sb.setSpan(clickableSpan, m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }
            binding.textTranslation.movementMethod = LinkMovementMethod.getInstance()
            binding.textTranslation.setText(sb, TextView.BufferType.SPANNABLE)
        }


        fun reverseAyahfromNumber(textQuran: String): String{

            val digits = mutableListOf<Char>()
            textQuran.forEach { char->
                if (char.isDigit()){
                    digits.add(char)
                }
            }
            val ayahNumberArabic = digits.joinToString("")
            val textWithoutNumber = textQuran.replace(ayahNumberArabic, "")
            return  textWithoutNumber + digits.reversed().joinToString("")
        }

    }
}