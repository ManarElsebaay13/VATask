package eg.com.test.vatask.ui.questionsResultActivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import eg.com.test.vatask.R
import eg.com.test.vatask.databinding.ActivityQuestionsResultBinding
import eg.com.test.vatask.ui.baseActivity.BaseActivity
import eg.com.test.vatask.util.LOCAL_PRODCAST_RECIEVER_UpdateQuestions

class QuestionsResultActivity :BaseActivity(
    true,
), QuestionsResultViewModel.Observer {

    lateinit var binding: ActivityQuestionsResultBinding

    override fun doOnCreate(arg0: Bundle?) {
        binding = putContentView(R.layout.activity_questions_result) as ActivityQuestionsResultBinding
        binding.viewModel =
                ViewModelProvider(
                        this,
                        QuestionsResultViewModelFactory(application)
                )
                        .get(QuestionsResultViewModel::class.java)
        binding.viewModel!!.baseViewModelObserver = baseViewModelObserver
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this
        initializeViews()
        setListener()
    }

    override fun initializeViews() {
    }

    override fun setListener() {
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        binding.viewModel?.getLocalQuestionModels()
    }

    var updateQuestionsBroadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            binding.viewModel?.getLocalQuestionModels()
        }
    }

    fun registerReceivers() {
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(
                        updateQuestionsBroadCastReceiver,
                        IntentFilter(
                                LOCAL_PRODCAST_RECIEVER_UpdateQuestions
                        )
                )
    }

    fun unRegisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                updateQuestionsBroadCastReceiver
        )
    }

    override fun onResume() {
        super.onResume()
        registerReceivers()
    }

    override fun onPause() {
        super.onPause()
        unRegisterReceivers()
    }

}