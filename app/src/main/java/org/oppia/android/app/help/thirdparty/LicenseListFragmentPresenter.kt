package org.oppia.android.app.help.thirdparty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import javax.inject.Inject
import org.oppia.android.app.fragment.FragmentScope
import org.oppia.android.app.recyclerview.BindableAdapter
import org.oppia.android.app.viewmodel.ViewModelProvider
import org.oppia.android.databinding.LicenseListFragmentBinding
import org.oppia.android.databinding.ThirdPartyDependencyItemBinding

/** The presenter for [LicenseListFragment]. */
@FragmentScope
class LicenseListFragmentPresenter(
  private val activity: AppCompatActivity,
  private val fragment: Fragment,
  private val viewModelProvider: ViewModelProvider<LicenseListViewModel>
) {
  private lateinit var binding: LicenseListFragmentBinding

  /** Handles onCreateView() method of the [LicenseListFragment]. */
  fun handleCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    thirdPartyDependencyIndex: Int
  ): View? {
//    val viewModel = getLicenseListViewModel(fragment, thirdPartyDependencyIndex)

    binding = LicenseListFragmentBinding.inflate(
      inflater,
      container,
      /* attachToRoot= */ false
    )

    binding.licenseListFragmentRecyclerView.apply {
      layoutManager = LinearLayoutManager(activity.applicationContext)
      adapter = createRecyclerViewAdapter()
    }

//    binding.let {
//      it.lifecycleOwner = fragment
//      it.viewModel = viewModel
//    }
    return binding.root
  }

  private fun createRecyclerViewAdapter(): BindableAdapter<ThirdPartyDependencyItemViewModel> {
    return BindableAdapter.SingleTypeBuilder
      .newBuilder<ThirdPartyDependencyItemViewModel>()
      .registerViewDataBinderWithSameModelType(
        inflateDataBinding = ThirdPartyDependencyItemBinding::inflate,
        setViewModel = ThirdPartyDependencyItemBinding::setViewModel
      )
      .build()
  }

//  private fun getLicenseListViewModel(fragment: Fragment, thirdPartyDependencyIndex: Int):
//    LicenseListViewModel {
//    return LicenseListViewModel(fragment, thirdPartyDependencyIndex)
//  }
}
