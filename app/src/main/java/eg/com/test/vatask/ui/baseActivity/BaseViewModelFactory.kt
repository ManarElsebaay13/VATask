package eg.com.test.vatask.ui.baseActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eg.com.test.vatask.app.MyApp

class BaseViewModelFactory (
    var application: MyApp
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BaseViewModel(application) as T
    }}