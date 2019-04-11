package com.kpiroom.bubble.ui.uploadScreen

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.FragmentUploadScreenBinding
import com.kpiroom.bubble.ui.progress.ProgressFragment
import com.kpiroom.bubble.ui.profileTabs.UploadsTabFragment.Companion.UPLOAD_URI
import com.kpiroom.bubble.util.livedata.observeTrue
import com.kpiroom.bubble.util.view.hideKeyboard

class UploadScreenFragment : ProgressFragment<UploadScreenLogic, FragmentUploadScreenBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        LayoutBuilder(inflater, container, R.layout.fragment_upload_screen) {
            logic = this@UploadScreenFragment.logic
        }

    override fun provideLogic() = ViewModelProviders.of(this).get(UploadScreenLogic::class.java)
}