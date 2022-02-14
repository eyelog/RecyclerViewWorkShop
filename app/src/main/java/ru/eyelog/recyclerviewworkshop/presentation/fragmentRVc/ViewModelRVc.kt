package ru.eyelog.recyclerviewworkshop.presentation.fragmentRVc

import android.util.Log
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
import ru.eyelog.recyclerviewworkshop.utils.toPx
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

    private var observable = Observable.interval(100L, TimeUnit.MILLISECONDS)
    private var observableTimeout = Observable.timer(100L, TimeUnit.MILLISECONDS)

    lateinit var infinityScrollDisposable: Disposable
    lateinit var disposableTimeout: Disposable
    lateinit var startSmoothDisposable: Disposable
    lateinit var stopSmoothDisposable: Disposable
    var isRollScrolling = false
    var isFinisherActive = false

    var targetPosition = 0
    var startPosition = 0
    lateinit var currentList: List<CardModel>
    var pathCounter = 0
    var itemCounter = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onCreate() {
        currentList = cardsFactory.getCards(9)
        _cardsLiveData.postValue(currentList)
        startPosition = ((Int.MAX_VALUE / 2) / currentList.size) * currentList.size - 2
        targetPosition = Random.nextInt(10)
        _setTargetPosition.postValue(targetPosition)
    }

    fun startScrolling() {
        if (!isRollScrolling) {
            isRollScrolling = true
            infinityScrollDisposable = observable.timeInterval()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    _scrollDy.postValue(-10)
//                    setPath(-10)
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

//    fun moveToTargetByStep(scipItems: Int, resources: Resources) {
//
//        val stepLength = resources.getDimension(R.dimen.barrelHeight).toPx / 5 * 2
//        Log.i("Logcat", "stepLength = $stepLength")
//        _scrollDy.postValue(-(stepLength * scipItems).toInt())
//
//    }

    fun moveToTargetByStep(itemHeight: Int, visiblePosition: Int) {

        infinityScrollDisposable.dispose()

        Log.i("Logcat", "eternal position ${Int.MAX_VALUE / 2}")

//        val firstPosition = ((Int.MAX_VALUE / 2) / currentList.size) * currentList.size
//        val localPosition = (Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) / currentList.size
//        Log.i("Logcat", "local firstPosition $firstPosition")
//        Log.i("Logcat", "local position $localPosition")

//        Log.i("Logcat", "itemHeight = $itemHeight")
//        Log.i("Logcat", "currentList.size = ${currentList.size}")
        Log.i("Logcat", "targetPosition = $targetPosition")

        val shiftFromStart = itemHeight  * (currentList.size * 10 - targetPosition)
        val progressFromStart = itemHeight  * (startPosition - visiblePosition)

        Log.i("Logcat", "shiftFromStart = $shiftFromStart")
        Log.i("Logcat", "progressFromStart = $progressFromStart")

//
//        val skipBlock = (itemHeight) * currentList.size * 10 + itemHeight * targetPosition
        val skipBlock = shiftFromStart - progressFromStart
//        val shiftPosition = (itemHeight) * targetPosition
//
        Log.i("Logcat", "skipBlock = $skipBlock")
//        Log.i("Logcat", "shiftPosition = $shiftPosition")
//        Log.i("Logcat", "pathCounter = $pathCounter")
//
//        val stepLength = skipBlock + shiftPosition
////        setPath(-stepLength)
//        Log.i("Logcat", "stepLength = $stepLength")
        _scrollDy.postValue(-skipBlock)

    }

    fun moveToTarget(itemHeight: Int) {
        val startPosition = pathCounter / itemHeight
        Log.i("Logcat", "startPosition $startPosition")
        isRollScrolling = false
        isFinisherActive = false
        val startSmoothEmitter = Observable.range(10, 70)
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
//                    setPath(dy)
                }
            }
    }

    fun stopSmooth() {
        val stopSmoothEmitter = Observable.range(10, 70)
            .concatMap { i: Int ->
                Observable.just(i)
                    .delay(100L, TimeUnit.MILLISECONDS)
                    .map { 80 - i }
            }
            .doFinally { stopSmoothDisposable.dispose() }
        stopSmoothDisposable = stopSmoothEmitter
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val dy = -(it.toDouble().pow(3) / 500).toInt()
                if (dy < 0) {
                    _scrollDy.postValue(dy)
//                    setPath(dy)
                }
            }
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

    fun setPath(path: Int) {
        pathCounter -= path
        Log.i("Logcat", "path = $pathCounter")
    }
}