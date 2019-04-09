package com.kpiroom.bubble.ui.uploadScreen

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.ActivityUploadScreenBinding
import com.kpiroom.bubble.ui.core.CoreFragment
import com.kpiroom.bubble.ui.main.MainActivity
import com.kpiroom.bubble.ui.tabs.UploadsTabFragment.Companion.UPLOAD_URI
import com.kpiroom.bubble.util.livedata.addSource
import com.kpiroom.bubble.util.livedata.observeTrue
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.view.hideKeyboard

class UploadScreenFragment : CoreFragment<UploadScreenLogic, ActivityUploadScreenBinding>() {

    private lateinit var parentProgressState: MediatorLiveData<ProgressState>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentProgressState = (activity as MainActivity).provideLogic().progress
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentProgressState.addSource(logic.progress)

        arguments?.getString(UPLOAD_URI)?.let {
            logic.renderPreview(Uri.parse(it))
        }

        logic.uploadClicked.observeTrue(this, Observer {
            view?.hideKeyboard()
        })

        logic.uploadComplete.observeTrue(this, Observer {
            findNavController().popBackStack()
        })
    }

    override fun provideLayout(inflater: LayoutInflater, container: ViewGroup?) =
        LayoutBuilder(inflater, container, R.layout.activity_upload_screen) {
            logic = this@UploadScreenFragment.logic
        }

    override fun provideLogic() = ViewModelProviders.of(this).get(UploadScreenLogic::class.java)
}