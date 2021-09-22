package eg.com.test.vatask.ui.questionsResultActivity

import androidx.lifecycle.MutableLiveData
import eg.com.test.vatask.adapter.QuestionsResultAdapter
import eg.com.test.vatask.app.MyApp
import eg.com.test.vatask.model.dp.DAO
import eg.com.test.vatask.model.entities.QuestionsModel
import eg.com.test.vatask.ui.baseActivity.BaseViewModel
import eg.com.test.vatask.util.OnRecyclerItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class QuestionsResultViewModel(
application: MyApp
) : BaseViewModel(application) {
    lateinit var observer: Observer
    var compositeDisposable = CompositeDisposable()
    val questionDAO: DAO = db.questionDao()

    var isShowLoader = MutableLiveData<Boolean>()
    var isShowNoData = MutableLiveData<Boolean>()

    var questionModels: ArrayList<QuestionsModel>? = ArrayList()
    var recyclerQuestionAdapter: QuestionsResultAdapter

    init {
        isShowLoader.value = true
        isShowNoData.value = false

        recyclerQuestionAdapter =
                QuestionsResultAdapter(questionModels!!, object : OnRecyclerItemClickListener {
                    override fun onRecyclerItemClickListener(position: Int) {

                    }

                })
        getLocalQuestionModels()
    }

    fun getLocalQuestionModels() {
        compositeDisposable.add(questionDAO.getQuestionModels()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                }
                .subscribe(
                        { dbQuestionList ->
                            isShowLoader.value = false
                            if (dbQuestionList.isEmpty()) {
                                isShowNoData.value = true
                            } else {
                                questionModels = dbQuestionList.toCollection(ArrayList())
                                recyclerQuestionAdapter.setList(questionModels!!)
                                isShowNoData.value = false
                            }
                        },
                        { }
                ))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    interface Observer {
    }

}