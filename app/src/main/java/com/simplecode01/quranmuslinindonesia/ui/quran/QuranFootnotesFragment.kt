package com.simplecode01.quranmuslinindonesia.ui.quran

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.simplecode01.quranmuslinindonesia.R
import com.simplecode01.quranmuslinindonesia.databinding.FootnotesBottomsheetBinding

class QuranFootnotesFragment : BottomSheetDialogFragment(){

    private  val binding : FootnotesBottomsheetBinding by viewBinding(R.id.linear_layout_footnotes)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.footnotes_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val footnotes =  arguments?.getString(KEY_FOOTNOTES)?: ""
        binding.textFootnotes.text = footnotes
        binding.toolbar.setOnMenuItemClickListener{
            findNavController().navigateUp()
        }
    }
    companion object{
        const val KEY_FOOTNOTES = "footnotes"
    }
}