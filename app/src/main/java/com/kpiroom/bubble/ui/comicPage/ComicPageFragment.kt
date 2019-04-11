package com.kpiroom.bubble.ui.comicPage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentComicPageBinding
import com.kpiroom.bubble.ui.progress.ProgressFragment

class ComicPageFragment : ProgressFragment<ComicPageLogic, FragmentComicPageBinding>() {
    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?): LayoutBuilder =
        LayoutBuilder(inflater, container, R.layout.fragment_comic_page) {
            logic = this@ComicPageFragment.logic
        }

    override fun provideLogic(): ComicPageLogic = ViewModelProviders.of(this).get(ComicPageLogic::class.java)
}