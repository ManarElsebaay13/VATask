package eg.com.test.vatask.ui.baseActivity

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import eg.com.test.vatask.app.MyApp
import eg.com.test.vatask.model.dp.AppDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

open class BaseViewModel (var application: MyApp
) : AndroidViewModel(application) {


    lateinit var baseViewModelObserver: BaseViewModelObserver
    var db: AppDatabase = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,
        "DB"
    ).build()

    var keyWord = MutableLiveData<String>()

    init {
        keyWord.value = ""
    }


    fun onButtonHomeClicked() {
        baseViewModelObserver.onHomeButtonClicked()
    }

    interface BaseViewModelObserver {
        fun onHomeButtonClicked()

    }
}