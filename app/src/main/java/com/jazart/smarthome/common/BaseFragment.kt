package com.jazart.smarthome.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jazart.smarthome.devicemgmt.getViewModel
import com.jazart.smarthome.di.Injectable
import com.jazart.smarthome.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var sharedUiViewModel: SharedUiViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedUiViewModel = getViewModel(viewModelFactory)
        observeLiveData()
    }

    abstract fun observeLiveData()

    abstract fun updateUi()

    fun showErrorBanner(error: Int) {
        sharedUiViewModel.showErrorBanner(getString(error))
    }

    fun showErrorToast(error: String) {

    }
}