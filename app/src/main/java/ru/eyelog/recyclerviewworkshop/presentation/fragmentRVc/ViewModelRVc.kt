package ru.eyelog.recyclerviewworkshop.presentation.fragmentRVc

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
import kotlin.math.pow
import kotlin.random.Random

@HiltViewModel
class ViewModelRVc @Inject constructor(
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

    val finishController: LiveData<Pair<Int, Int>> get() = _finishController
    private val _finishController = MediatorLiveData<Pair<Int, Int>>()

    private var observable = Observable.interval(100L, TimeUnit.MILLISECONDS)
    private var observableTimeout = Observable.timer(100L, TimeUnit.MILLISECONDS)

    lateinit var infinityScrollDisposable: Disposable
    lateinit var disposableTimeout: Disposable
    lateinit var startSmoothDisposable: Disposable
    lateinit var stopSmoothDisposable: Disposable
    var isRollScrolling = false

    var targetPosition = 0
    var startPosition = 0
    lateinit var currentList: List<CardModel>

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() {
        currentList = cardsFactory.getCards(4)
        _cardsLiveData.postValue(currentList)
        startPosition = ((Int.MAX_VALUE / 2) / currentList.size) * currentList.size - 2
        targetPosition = Random.nextInt(4)
        _setTargetPosition.postValue(targetPosition)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() {
        if (this::infinityScrollDisposable.isInitialized) {
            infinityScrollDisposable.dispose()
        }
        if (this::disposableTimeout.isInitialized) {
            disposableTimeout.dispose()
        }
        if (this::startSmoothDisposable.isInitialized) {
            startSmoothDisposable.dispose()
        }
        if (this::stopSmoothDisposable.isInitialized) {
            stopSmoothDisposable.dispose()
        }
    }

    fun startScrolling() {
        if (!isRollScrolling) {
            isRollScrolling = true
            infinityScrollDisposable = observable.timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _scrollDy.postValue(-10)
                }
        }
    }

    fun startFirstPing() {
        disposableTimeout = observableTimeout
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _updateVheel.postValue(true)
            }
    }

    fun moveToTarget(itemHeight: Int, visiblePosition: Int) {
        isRollScrolling = false
        val shiftFromStart = itemHeight * (currentList.size * 10 - targetPosition)
        val progressFromStart = itemHeight * (startPosition - visiblePosition)
        val skipBlock = shiftFromStart - progressFromStart

        val startSmoothEmitter = Observable.range(20, 70)
            .concatMap { i: Int ->
                Observable.just(i)
                    .delay(100L, TimeUnit.MILLISECONDS)
                    .map { it }
            }
            .doOnSubscribe {
                infinityScrollDisposable.dispose()
            }
            .doOnComplete {
                startSmoothDisposable.dispose()
            }
        startSmoothDisposable = startSmoothEmitter
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { stopSmooth() }
            .subscribe {
                val dy = -(it.toDouble().pow(3) / 500).toInt()
                if (dy < 0) {
                    _scrollDy.postValue(dy)
                }
            }
    }

    private fun stopSmooth() {
        val stopSmoothEmitter = Observable.range(10, 100)
            .concatMap { i: Int ->
                Observable.just(i)
                    .delay(100L, TimeUnit.MILLISECONDS)
            }
            .doFinally {
                stopSmoothDisposable.dispose()
            }
        stopSmoothDisposable = stopSmoothEmitter
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it > 50) {
                    _finishController.postValue(targetPosition to currentList.size)
                }
                val dy = -(3000 / it + 30)

                if (dy < 0) {
                    _scrollDy.postValue(dy)
                }
            }
    }

    fun stopFinisher() {
        stopSmoothDisposable.dispose()
        infinityScrollDisposable.dispose()
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