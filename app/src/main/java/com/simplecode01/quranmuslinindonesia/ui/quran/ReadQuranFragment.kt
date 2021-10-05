package com.simplecode01.quranmuslinindonesia.ui.quran


import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.control.RepeatMode
import com.simplecode01.quranmuslinindonesia.R
import com.simplecode01.quranmuslinindonesia.data.QuranDatabase
import com.simplecode01.quranmuslinindonesia.databinding.FragmentReadQuranBinding
import com.simplecode01.quranmuslinindonesia.model.Quran
import android.view.MenuItem as MenuItem1


class ReadQuranFragment: Fragment(R.layout.fragment_read_quran) {

    val binding: FragmentReadQuranBinding by viewBinding()
    private val viewModel: QuranViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAudioConfig()
        val dataPaketSurahNumber = arguments?.getInt(KEY_SURAH_NUMBER) ?: 1
        val dataSurahName = arguments?.getString(KEY_AYAH_NAME)?: ""
        val dataPaketJuzNumber = arguments?.getInt(KEY_JUZ_NUMBER) ?: 1
        val dataPaketPageNumber = arguments?.getInt(KEY_PAGE_NUMBER) ?: 1

        viewModel.getTabPosisition().observe(viewLifecycleOwner, { tabPosition ->
            viewModel.getTotalAyahList().observe(viewLifecycleOwner,{totalAyahlist ->
                val database = QuranDatabase.getInstance(requireContext())
                val quranDao = database.quranDao()
                var titleToolBar: String = ""
                when(tabPosition){
                    TAB_SURAH ->{
                        quranDao.getSurahByNumber(dataPaketSurahNumber).asLiveData().observe(viewLifecycleOwner, {quranList ->
                            setQuranAdapter(quranList, totalAyahlist)
                        })
                        titleToolBar = "Qs. $dataSurahName"
                    }
                    TAB_JUZ ->{
                        quranDao.getJuzByNumber(dataPaketJuzNumber).asLiveData().observe(viewLifecycleOwner, {quranList ->
                            setQuranAdapter(quranList, totalAyahlist)
                        })
                        titleToolBar = "Juz $dataPaketJuzNumber"
                    }
                    TAB_PAGE ->{
                        quranDao.getPageByNumber(dataPaketPageNumber).asLiveData().observe(viewLifecycleOwner, {quranList ->
                            setQuranAdapter(quranList, totalAyahlist)
                        })
                        titleToolBar = "Page $dataPaketPageNumber"
                    }
                }
                val toolbarActivity = requireActivity().findViewById<Toolbar>(R.id.toolbar)
                toolbarActivity.title = titleToolBar
            })
        })
    }

    private fun setUpAudioConfig() = StarrySky.with().apply{
        StarrySky.openNotification()
        setRepeatMode(RepeatMode.REPEAT_MODE_NONE, false)
    }

    private fun setQuranAdapter(quranList: List<Quran>, ayahTotal: List<Int>) {
        val adapter: ReadQuranAdapter = ReadQuranAdapter(quranList, ayahTotal)
        binding.recyclerview.adapter = adapter

        adapter.footnotesOnClickListener={quran ->
            val bundle = bundleOf(QuranFootnotesFragment.KEY_FOOTNOTES to quran.footnotes)
            findNavController().navigate(R.id.action_nav_read_quran_to_nav_quran_footnotes, bundle)
        }

        adapter.moreCLickListener={quran, totalAyah->

            val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
            val dialog = BottomSheetDialog(requireContext())
            dialog.setCancelable(true)
            dialog.setContentView(view)

            val btnCopy = view.findViewById<LinearLayout>(R.id.copyAyah)
            val btnShare = view.findViewById<LinearLayout>(R.id.shareAyah)
            val btnPlayAyah = view.findViewById<LinearLayout>(R.id.playAyah)
            val btnPlaySurah = view.findViewById<LinearLayout>(R.id.playFullSurah)


            btnCopy.setOnClickListener{
                val copyContent = "Quran Ayat : ${quran.textQuran}\n\nQuran Translate : ${quran.translation}\n\n(Q.S ${quran.surahName}[${quran.surahNumber}]: ${quran.ayahNumber}))"
                val clipboard: ClipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE)as ClipboardManager
                val clip = ClipData.newPlainText("copy_ayah", copyContent)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(requireContext(), "Copy Qs. ${quran.surahName}[${quran.surahNumber}]: ${quran.ayahNumber}", Toast.LENGTH_SHORT)
                    .show();
                dialog.dismiss()
            }
            btnShare.setOnClickListener {
                val shareContent = "Quran Ayat : ${quran.textQuran}\n\nQuran Translate : ${quran.translation}\n\n (Q.s ${quran.surahName}[${quran.surahNumber}]: ${quran.ayahNumber})"
                ShareCompat.IntentBuilder(requireContext())
                    .setText(shareContent)
                    .setType("text/plain")
                    .startChooser()
                Toast.makeText(requireContext(), "Share Qs. ${quran.surahName}[${quran.surahNumber}]: ${quran.ayahNumber}", Toast.LENGTH_SHORT)
                    .show();
                dialog.dismiss()
            }
            btnPlayAyah.setOnClickListener {

                val view = layoutInflater.inflate(R.layout.bottom_sheet_listening, null)
                val dialog_listening = BottomSheetDialog(requireContext())
                dialog_listening.setCancelable(true)
                dialog_listening.setContentView(view)

                val btnImam1 = view.findViewById<LinearLayout>(R.id.imam1)
                val btnImam2 = view.findViewById<LinearLayout>(R.id.imam2)
                val btnImam3 = view.findViewById<LinearLayout>(R.id.imam3)
                val btnImam4 = view.findViewById<LinearLayout>(R.id.imam4)
                val btnImam5 = view.findViewById<LinearLayout>(R.id.imam5)

                btnImam1.setOnClickListener {

                    val audioImam = SongInfo()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    val ayahNumberFormat = String.format("%03d", quran.ayahNumber)
                    val audioURLHD = "https://archive.org/download/quran-every-ayah/Abdurrahman%20as-Sudais_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                    audioImam.songName = "Qs. ${quran.surahName}: ${quran.ayahNumber}"
                    audioImam.songUrl = audioURLHD
                    audioImam.songCover = "https://www.madaninews.id/wp-content/uploads/2018/07/Abdul-Rahman-Al-Sudais-at-digital-mode-by-syed-noman-zafar-855x1024.jpg"
                    audioImam.songId = quran.id.toString()
                    audioImam.artist = "Syeikh Abdurrahman As-Sudais"
                    StarrySky.with().playMusicByInfo(audioImam)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}[${quran.surahNumber}]: ${quran.ayahNumber}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }
                btnImam2.setOnClickListener {
                    val audioImam = SongInfo()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    val ayahNumberFormat = String.format("%03d", quran.ayahNumber)
                    val audioURLHD = "https://archive.org/download/quran-every-ayah/Abdullah%20Basfar_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                    audioImam.songName = "Qs. ${quran.surahName}: ${quran.ayahNumber}"
                    audioImam.songUrl = audioURLHD
                    audioImam.songCover = "http://3.bp.blogspot.com/-gTq-PJCtte4/WT9QikiTibI/AAAAAAAARlg/C1S3JCDgiu4TwwBy8TM_bW00ALZ1-siAQCK4B/s1600/Abdullah-Ibn-Ali-Basfar-%25D8%25B9%25D8%25A8%25D8%25AF%2B%25D8%25A7%25D9%2584%25D9%2584%25D9%2587%2B%25D8%25A8%25D9%2586%2B%25D8%25B9%25D9%2584%25D9%258A%2B%25D8%25A8%25D8%25B5%25D9%2581%25D8%25B1.jpg"
                    audioImam.songId = quran.id.toString()
                    audioImam.artist = "Abdullah Ibn Ali Basfar"
                    StarrySky.with().playMusicByInfo(audioImam)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}[${quran.surahNumber}]: ${quran.ayahNumber}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }
                btnImam3.setOnClickListener {
                    val audioImam = SongInfo()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    val ayahNumberFormat = String.format("%03d", quran.ayahNumber)
                    val audioURLHD = "https://archive.org/download/quran-every-ayah/Abu%20Bakr%20Ashatri_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                    audioImam.songName = "Qs. ${quran.surahName}: ${quran.ayahNumber}"
                    audioImam.songUrl = audioURLHD
                    audioImam.songCover = "https://i1.sndcdn.com/artworks-000387234564-j0hxuf-t500x500.jpg"
                    audioImam.songId = quran.id.toString()
                    audioImam.artist = "Sheikh Abu Bakr Al Shatri"
                    StarrySky.with().playMusicByInfo(audioImam)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}[${quran.surahNumber}]: ${quran.ayahNumber}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }
                btnImam4.setOnClickListener {
                    val audioImam = SongInfo()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    val ayahNumberFormat = String.format("%03d", quran.ayahNumber)
                    val audioURLHD = "https://archive.org/download/quran-every-ayah/Ahmed%20ibn%20Ali%20Al-Ajamy_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                    audioImam.songName = "Qs. ${quran.surahName}: ${quran.ayahNumber}"
                    audioImam.songUrl = audioURLHD
                    audioImam.songCover = "https://alquranonline.net/wp-content/uploads/2021/06/Assabile-Ahmed-Al-Ajmi-FULL-MP3.jpg"
                    audioImam.songId = quran.id.toString()
                    audioImam.artist = "Ahmad bin Ali Al-Ajmi"
                    StarrySky.with().playMusicByInfo(audioImam)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}[${quran.surahNumber}]: ${quran.ayahNumber}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }
                btnImam5.setOnClickListener {
                    val audioImam = SongInfo()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    val ayahNumberFormat = String.format("%03d", quran.ayahNumber)
                    val audioURLHD = "https://archive.org/download/quran-every-ayah/Mishary%20Rashid%20Alafasy_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                    audioImam.songName = "Qs. ${quran.surahName}: ${quran.ayahNumber}"
                    audioImam.songUrl = audioURLHD
                    audioImam.songCover = "https://upload.wikimedia.org/wikipedia/commons/2/24/%D0%9C%D0%B8%D1%88%D0%B0%D1%80%D0%B8_%D0%A0%D0%B0%D1%88%D0%B8%D0%B4.jpg"
                    audioImam.songId = quran.id.toString()
                    audioImam.artist = "Shaykh Mishari Alafasy"
                    StarrySky.with().playMusicByInfo(audioImam)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}[${quran.surahNumber}]: ${quran.ayahNumber}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }

                dialog_listening.show()
            }
            btnPlaySurah.setOnClickListener {

                val view = layoutInflater.inflate(R.layout.bottom_sheet_listening, null)
                val dialog_listening = BottomSheetDialog(requireContext())
                dialog_listening.setCancelable(true)
                dialog_listening.setContentView(view)
                val btnImam1 = view.findViewById<LinearLayout>(R.id.imam1)
                val btnImam2 = view.findViewById<LinearLayout>(R.id.imam2)
                val btnImam3 = view.findViewById<LinearLayout>(R.id.imam3)
                val btnImam4 = view.findViewById<LinearLayout>(R.id.imam4)
                val btnImam5 = view.findViewById<LinearLayout>(R.id.imam5)

                btnImam1.setOnClickListener {
                    val audioList = mutableListOf<SongInfo>()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    for (i in 1..totalAyah){
                        val audio = SongInfo()
                        val ayahNumberFormat = String.format("%03d", i)
                        val audioURLHD = "https://archive.org/download/quran-every-ayah/Abdurrahman%20as-Sudais_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                        audio.songName = "Qs. ${quran.surahName}: ${i}"
                        audio.songUrl = audioURLHD
                        audio.songCover = "https://www.madaninews.id/wp-content/uploads/2018/07/Abdul-Rahman-Al-Sudais-at-digital-mode-by-syed-noman-zafar-855x1024.jpg"
                        audio.songId = surahNumberFormat + ayahNumberFormat
                        audio.artist = "Syeikh Abdurrahman As-Sudais"
                        audioList.add(audio)
                    }
                    val indexStartPlay = quran.ayahNumber!! - 1
                    StarrySky.with().playMusic(audioList, indexStartPlay)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }
                btnImam2.setOnClickListener {
                    val audioList = mutableListOf<SongInfo>()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    for (i in 1..totalAyah){
                        val audio = SongInfo()
                        val ayahNumberFormat = String.format("%03d", i)
                        val audioURLHD = "https://archive.org/download/quran-every-ayah/Abdullah%20Basfar_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                        audio.songName = "Qs. ${quran.surahName}: ${i}"
                        audio.songUrl = audioURLHD
                        audio.songCover = "http://3.bp.blogspot.com/-gTq-PJCtte4/WT9QikiTibI/AAAAAAAARlg/C1S3JCDgiu4TwwBy8TM_bW00ALZ1-siAQCK4B/s1600/Abdullah-Ibn-Ali-Basfar-%25D8%25B9%25D8%25A8%25D8%25AF%2B%25D8%25A7%25D9%2584%25D9%2584%25D9%2587%2B%25D8%25A8%25D9%2586%2B%25D8%25B9%25D9%2584%25D9%258A%2B%25D8%25A8%25D8%25B5%25D9%2581%25D8%25B1.jpg"
                        audio.songId = surahNumberFormat + ayahNumberFormat
                        audio.artist = "Abdullah Ibn Ali Basfar"
                        audioList.add(audio)
                    }
                    val indexStartPlay = quran.ayahNumber!! - 1
                    StarrySky.with().playMusic(audioList, indexStartPlay)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }
                btnImam3.setOnClickListener {
                    val audioList = mutableListOf<SongInfo>()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    for (i in 1..totalAyah){
                        val audio = SongInfo()
                        val ayahNumberFormat = String.format("%03d", i)
                        val audioURLHD = "https://archive.org/download/quran-every-ayah/Abu%20Bakr%20Ashatri_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                        audio.songName = "Qs. ${quran.surahName}: ${i}"
                        audio.songUrl = audioURLHD
                        audio.songCover = "https://i1.sndcdn.com/artworks-000387234564-j0hxuf-t500x500.jpg"
                        audio.songId = surahNumberFormat + ayahNumberFormat
                        audio.artist = "Sheikh Abu Bakr Al Shatri"
                        audioList.add(audio)
                    }
                    val indexStartPlay = quran.ayahNumber!! - 1
                    StarrySky.with().playMusic(audioList, indexStartPlay)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }
                btnImam4.setOnClickListener {
                    val audioList = mutableListOf<SongInfo>()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    for (i in 1..totalAyah){
                        val audio = SongInfo()
                        val ayahNumberFormat = String.format("%03d", i)
                        val audioURLHD = "https://archive.org/download/quran-every-ayah/Ahmed%20ibn%20Ali%20Al-Ajamy_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                        audio.songName = "Qs. ${quran.surahName}: ${i}"
                        audio.songUrl = audioURLHD
                        audio.songCover = "https://alquranonline.net/wp-content/uploads/2021/06/Assabile-Ahmed-Al-Ajmi-FULL-MP3.jpg"
                        audio.songId = surahNumberFormat + ayahNumberFormat
                        audio.artist = "Ahmad bin Ali Al-Ajmi"
                        audioList.add(audio)
                    }
                    val indexStartPlay = quran.ayahNumber!! - 1
                    StarrySky.with().playMusic(audioList, indexStartPlay)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }
                btnImam5.setOnClickListener {
                    val audioList = mutableListOf<SongInfo>()
                    val surahNumberFormat = String.format("%03d", quran.surahNumber)
                    for (i in 1..totalAyah){
                        val audio = SongInfo()
                        val ayahNumberFormat = String.format("%03d", i)
                        val audioURLHD = "https://archive.org/download/quran-every-ayah/Mishary%20Rashid%20Alafasy_HQ.zip/$surahNumberFormat$ayahNumberFormat.mp3"
                        audio.songName = "Qs. ${quran.surahName}: ${i}"
                        audio.songUrl = audioURLHD
                        audio.songCover = "https://upload.wikimedia.org/wikipedia/commons/2/24/%D0%9C%D0%B8%D1%88%D0%B0%D1%80%D0%B8_%D0%A0%D0%B0%D1%88%D0%B8%D0%B4.jpg"
                        audio.songId = surahNumberFormat + ayahNumberFormat
                        audio.artist = "Shaykh Mishari Alafasy"
                        audioList.add(audio)
                    }
                    val indexStartPlay = quran.ayahNumber!! - 1
                    StarrySky.with().playMusic(audioList, indexStartPlay)

                    Toast.makeText(requireContext(), "Play Qs. ${quran.surahName}", Toast.LENGTH_SHORT)
                        .show();
                    dialog.dismiss()
                    dialog_listening.dismiss()
                }

                dialog_listening.show()
            }
            dialog.show()
        }
    }

    companion object{
        const val KEY_AYAH_NAME = "ayah_total"
        const val KEY_SURAH_NUMBER = "surah_number"
        const val KEY_JUZ_NUMBER = "juz_number"
        const val KEY_PAGE_NUMBER = "page_number"
        const val TAB_SURAH = 0
        const val TAB_JUZ = 1
        const val TAB_PAGE = 2
    }

}