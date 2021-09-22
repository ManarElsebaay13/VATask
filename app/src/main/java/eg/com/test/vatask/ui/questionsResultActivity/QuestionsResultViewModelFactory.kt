package eg.com.test.vatask.ui.questionsResultActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.com.test.vatask.app.MyApp

class QuestionsResultViewModelFactory(
        var application: MyApp
) :
        ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionsResultViewModel(application) as T
    }
}