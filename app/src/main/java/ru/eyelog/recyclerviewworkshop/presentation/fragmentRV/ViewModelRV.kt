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
import kotlin.random.Random

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

    val updateVheel: LiveData<Boolean> get() = _updateVheel
    private val _updateVheel = MediatorLiveData<Boolean>()

    val setTargetPosition: LiveData<Int> get() = _setTargetPosition
    private val _setTargetPosition = MediatorLiveData<Int>()

    val removeScrollListener: LiveData<Boolean> get() = _removeScrollListener
    private val _removeScrollListener = MediatorLiveData<Boolean>()

    private var observable = Observable.interval(100L, TimeUnit.MILLISECONDS)
    private var observableTimeout = Observable.timer(100L, TimeUnit.MILLISECONDS)

    lateinit var disposable: Disposable
    lateinit var disposableTimeout: Disposable
    lateinit var smoothDisposable: Disposable
    var isRollScrolling = false
    var isFinisherActive = false

    var targetPosition = 0
    lateinit var currentList: List<CardModel>

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onCreate() {
        currentList = cardsFactory.getCards(10)
        _cardsLiveData.postValue(currentList)
        targetPosition = Random.nextInt(10)
        _setTargetPosition.postValue(targetPosition)
    }

    fun startScrolling() {
        if (!isRollScrolling) {
            isRollScrolling = true
            disposable = observable.timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _scrollDy.postValue(-10)
                }
        }
    }

    fun startSmoothFinisher(position: Int) {
//        if (!isFinisherActive){
//            isFinisherActive = true
            val smoothEmitter = Observable.range(1, 5)
                .concatMap { i: Int ->
                    Observable.just(
                        i
                    ).delay(300, TimeUnit.MILLISECONDS)
                }
                .doOnComplete {
                    smoothDisposable.dispose()
                    _removeScrollListener.postValue(true)
                }

            smoothDisposable = smoothEmitter
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _scrollPosition.postValue(position - it)
                }
//        }
    }

    fun startFirstPing() {
        disposableTimeout = observableTimeout.timeout(300L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _updateVheel.postValue(true)
            }
    }

    fun moveToTarget() {
        isRollScrolling = false
        isFinisherActive = false
        disposable.dispose()
        _scrollPosition.postValue(Int.MAX_VALUE / 2 - currentList.size * 10 + targetPosition - 2)
    }

    private fun getFirstNumberAfterDot(a: Int, b: Int): Int {
        val z = a.toDouble() / b.toDouble()
        val str = z.toString()
        val ind = str.indexOf('.')
        return str[ind + 1].digitToInt()
    }

    fun setCurrentPosition(position: Int) {
        currentPosition = position
    }
}