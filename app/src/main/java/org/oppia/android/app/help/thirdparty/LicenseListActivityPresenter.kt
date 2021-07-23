package org.oppia.android.app.help.thirdparty

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import org.oppia.android.R
import org.oppia.android.app.activity.ActivityScope
import org.oppia.android.databinding.LicenseListActivityBinding
import javax.inject.Inject

/** The presenter for [LicenseListActivity]. */
@ActivityScope
class LicenseListActivityPresenter @Inject constructor(
  private val activity: AppCompatActivity
) {
  private lateinit var licenseListActivityToolbar: Toolbar

  /** Handles onCreate() method of the [LicenseListActivity]. */
  fun handleOnCreate() {
    val binding =
      DataBindingUtil.setContentView<LicenseListActivityBinding>(
        activity,
        R.layout.license_list_activity
      )
    binding.apply {
      lifecycleOwner = activity
    }

    licenseListActivityToolbar = binding.licenseListActivityToolbar
    activity.setSupportActionBar(licenseListActivityToolbar)
    activity.supportActionBar!!.title = activity.getString(R.string.licenses)
    activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
    activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    binding.licenseListActivityToolbar.setNavigationOnClickListener {
      (activity as LicenseListActivity).finish()
    }

    if (getThirdPartyDependencyListFragment() == null) {
      activity.supportFragmentManager.beginTransaction().add(
        R.id.license_list_fragment_placeholder,
        LicenseListFragment()
      ).commitNow()
    }
  }

  private fun getThirdPartyDependencyListFragment(): LicenseListFragment? {
    return activity
      .supportFragmentManager
      .findFragmentById(R.id.license_list_fragment_placeholder) as LicenseListFragment?
  }
}
