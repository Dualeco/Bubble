package com.kpiroom.bubble.ui.comicPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentComicPageBinding
import com.kpiroom.bubble.ui.userPage.UserPageFragment.Companion.ARG_USER
import com.kpiroom.bubble.ui.pdfViewer.PdfViewerActivity.Companion.ARG_COMIC_URI
import com.kpiroom.bubble.ui.progress.ProgressFragment
import com.kpiroom.bubble.util.constants.col
import com.kpiroom.bubble.util.constants.drw
import com.kpiroom.bubble.util.livedata.observeNotNull
import com.kpiroom.bubble.util.livedata.observeTrue
import com.kpiroom.bubble.util.recyclerview.model.ComicPage
import kotlinx.android.synthetic.main.fragment_comic_page.*

class ComicPageFragment : ProgressFragment<ComicPageLogic, FragmentComicPageBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logic.comicPage = arguments?.getParcelable(ARG_COMIC_PAGE) ?: ComicPage()

        logic.ownProfileClicked.observeTrue(this, Observer {
            findNavController().navigate(R.id.action_open_own_page)
        })

        logic.otherAuthorClicked.observeNotNull(this, Observer {
            findNavController().navigate(R.id.action_open_comic_author, bundleOf(ARG_USER to it))
        })

        logic.starred.observeNotNull(this, Observer {
            comicStarIcon.setImageDrawable(drw(if (it) R.drawable.ic_star_red else R.drawable.ic_star))
            comicStarCount.setTextColor(col(if (it) R.color.colorPrimaryRed else R.color.colorGrey))
        })

        logic.openedPdfUri.observeNotNull(this, Observer {
            findNavController().navigate(R.id.action_read_comic, bundleOf(ARG_COMIC_URI to it.toString()))
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        logic.initStars()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?): LayoutBuilder =
        LayoutBuilder(inflater, container, R.layout.fragment_comic_page) {
            logic = this@ComicPageFragment.logic
            comicPage = this@ComicPageFragment.logic.comicPage
        }

    override fun provideLogic(): ComicPageLogic = ViewModelProviders.of(this).get(ComicPageLogic::class.java)

    companion object {
        val ARG_COMIC_PAGE = "comicPage"
    }
}