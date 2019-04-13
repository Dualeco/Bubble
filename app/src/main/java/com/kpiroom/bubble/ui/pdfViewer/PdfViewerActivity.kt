package com.kpiroom.bubble.ui.pdfViewer

import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.kpiroom.bubble.R
import kotlinx.android.synthetic.main.activity_pdf_viewer.*

class PdfViewerActivity : AppCompatActivity() {

    private var curPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        curPage = savedInstanceState?.getInt(KEY_CUR_PAGE) ?: 0

        val isOrientationPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

        pdfView.fromUri(Uri.parse(intent.extras?.getString(ARG_COMIC_URI)))
            .defaultPage(curPage)
            .enableSwipe(true)
            .swipeHorizontal(true)
            .pageFitPolicy(if (isOrientationPortrait) FitPolicy.WIDTH else FitPolicy.HEIGHT)
            .pageSnap(true)
            .pageFling(true)
            .onPageChange { page, _ -> curPage = page }
            .load()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(KEY_CUR_PAGE, curPage)
    }

    companion object {
        private const val KEY_CUR_PAGE = "curPage"
        const val ARG_COMIC_URI = "comicUri"
    }
}