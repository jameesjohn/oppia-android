package org.oppia.android.app.help.thirdparty

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.oppia.android.app.fragment.InjectableFragment
import javax.inject.Inject


private const val THIRD_PARTY_DEPENDENCY_INDEX =
  "LicenseListFragment.thirdPartyDependencyIndex"
/** Fragment that contains list of licenses for a third-party in the app. */
class LicenseListFragment : InjectableFragment() {

  @Inject
  lateinit var licenseListFragmentPresenter: LicenseListFragmentPresenter

  companion object {
    fun newInstance(thirdPartyDependencyIndex: Int): LicenseListFragment {
      val licenseListFragment = LicenseListFragment()
      val args = Bundle()
      args.putInt(THIRD_PARTY_DEPENDENCY_INDEX, thirdPartyDependencyIndex)
      licenseListFragment.arguments = args
      return licenseListFragment
    }
  }

  override fun onAttach(context: Context) {
    super.onAttach(context)
    fragmentComponent.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val args =
      checkNotNull(arguments) { "Expected arguments to be passed to LicenseListFragment" }
    val thirdPartyDependencyIndex = args.getInt(
      THIRD_PARTY_DEPENDENCY_INDEX, -1
    )
    return licenseListFragmentPresenter.handleCreateView(inflater, container, thirdPartyDependencyIndex)
  }
}
