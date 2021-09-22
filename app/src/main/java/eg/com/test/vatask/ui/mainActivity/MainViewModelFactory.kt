package eg.com.test.vatask.ui.mainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.com.test.vatask.app.MyApp

class MainViewModelFactory  (
        var application: MyApp
) :
ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}


