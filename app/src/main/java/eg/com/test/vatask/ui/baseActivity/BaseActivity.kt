package eg.com.test.vatask.ui.baseActivity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import eg.com.test.vatask.R
import eg.com.test.vatask.app.MyApp
import eg.com.test.vatask.databinding.ActivityBaseBinding
import eg.com.test.vatask.ui.mainActivity.MainActivity
import eg.com.test.vatask.ui.questionsResultActivity.QuestionsResultActivity
import eg.com.test.vatask.util.OnAskUserAction
import eg.com.test.vatask.util.ProgressDialogLoading
import eg.com.test.vatask.util.showMessage
import kotlin.properties.Delegates

abstract class BaseActivity(
        var showHomeButton: Boolean,
): AppCompatActivity() {

    protected abstract fun doOnCreate(arg0: Bundle?)
    abstract fun initializeViews()
    abstract fun setListener()
    lateinit var baseBinding: ActivityBaseBinding
    var application: MyApp by Delegates.notNull()
    var imm: InputMethodManager by Delegates.notNull()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)

        setHomeButtonVisibility(showHomeButton)
        application = getApplication() as MyApp
        application.context = this

        baseBinding.viewModel =
            ViewModelProvider(this, BaseViewModelFactory(application))
                .get(BaseViewModel::class.java)
        baseBinding.viewModel!!.baseViewModelObserver = baseViewModelObserver
        baseBinding.lifecycleOwner = this
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        doOnCreate(savedInstanceState)
        setListener()
    }

    var baseViewModelObserver = object : BaseViewModel.BaseViewModelObserver {

        override fun onHomeButtonClicked() {

            Intent(this@BaseActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    //display view
    fun putContentView(activityLayout: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            layoutInflater,
            activityLayout,
            baseBinding.baseFragment,
            true
        )
    }


    open fun finish_activity() {
        finish()
    }

    fun setHomeButtonVisibility(isVisible: Boolean) {
        baseBinding.ivHomeButton.visibility =
                if (isVisible) View.VISIBLE else View.GONE
        if (isVisible) {
            val anim = ScaleAnimation(
                    0.8f, 1f,  // Start and end values for the X axis scaling
                    0.8f, 1f,  // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f
            ) // Pivot point of Y scaling
            // Needed to keep the result of the animation
            anim.fillAfter = true
            anim.duration = 100
            anim.repeatCount = 8
            anim.repeatMode = Animation.REVERSE
            baseBinding.ivHomeButton.startAnimation(anim)
        }
    }

    fun showHideMessageDialog(isShow: Boolean, title: String, message: String) {
        if (isShow)
            showMessage(
                    this@BaseActivity, title,
                    message,
                    object : OnAskUserAction {
                        override fun onPositiveAction() {
                        }

                        override fun onNegativeAction() {
                        }

                    },
                    false,
                    getString(R.string.cancel),
                    getString(R.string.ok),
                    true
            )
        else
            ProgressDialogLoading.dismiss(this@BaseActivity)
    }

}