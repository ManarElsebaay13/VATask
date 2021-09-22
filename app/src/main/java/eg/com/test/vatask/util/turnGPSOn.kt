package eg.com.test.vatask.util

import androidx.appcompat.app.AppCompatActivity
import eg.com.test.vatask.R

fun turnGPSOn(activity: AppCompatActivity, onAskUserAction: OnAskUserAction) {
    showMessage(
        activity,
        activity.getString(R.string.location_required),
        activity.getString(R.string.you_must_enable_device_location),
        object : OnAskUserAction {
            override fun onPositiveAction() {
                onAskUserAction.onPositiveAction()
            }

            override fun onNegativeAction() {
            }
        },
        false,
        activity.getString(R.string.cancel),
        activity.getString(R.string.ok),
        false
    )
}