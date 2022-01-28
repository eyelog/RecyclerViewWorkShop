package ru.eyelog.recyclerviewworkshop.presentation.fragmentRV

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.eyelog.recyclerviewworkshop.data.CardModel
import ru.eyelog.recyclerviewworkshop.presentation.factory.CardsFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ViewModelRV @Inject constructor(
    private val cardsFactory: CardsFactory
) : ViewModel(), LifecycleObserver {

    private var currentPosition = 0

    val cardsLiveData: LiveData<List<CardModel>> get() = _cardsLiveData
    private val _cardsLiveData = MediatorLiveData<List<CardModel>>()

    val scrollPosition: LiveData<Int> get() = _scrollPosition
    private val _scrollPosition = MediatorLiveData<Int>()

    val scrollDy: LiveData<Int> get() = _scrollDy
    private val _scrollDy = MediatorLiveData<Int>()

    private var observable = Observable.interval(100L, TimeUnit.MILLISECONDS)

    lateinit var disposable: Disposable

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        _cardsLiveData.postValue(cardsFactory.getCars(10))
    }

    fun startScrolling(){
        disposable = observable.timeInterval()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _scrollDy.postValue(30)
            }
    }

    fun moveToTarget() {
        disposable.dispose()
        _scrollPosition.postValue(currentPosition + 150)
    }

    fun setCurrentPosition(position: Int){
        currentPosition = position
    }
}