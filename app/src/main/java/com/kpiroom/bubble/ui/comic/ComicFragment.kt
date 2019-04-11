package com.kpiroom.bubble.ui.comic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentComicBinding
import com.kpiroom.bubble.ui.core.CoreFragment
import com.kpiroom.bubble.ui.progress.ProgressFragment

class ComicFragment : ProgressFragment<ComicLogic, FragmentComicBinding>() {
    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?): LayoutBuilder =
        LayoutBuilder(inflater, container, R.layout.fragment_comic) {
            logic = this@ComicFragment.logic
        }

    override fun provideLogic(): ComicLogic = ViewModelProviders.of(this).get(ComicLogic::class.java)
}