package ru.eyelog.recyclerviewworkshop.presentation.fragmentRVa

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
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.pow
import kotlin.random.Random

@HiltViewModel
class ViewModelRVa @Inject constructor(
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

    lateinit var disposable: Disposable
    lateinit var disposableTimeout: Disposable
    lateinit var startSmoothDisposable: Disposable
    lateinit var stopSmoothDisposable: Disposable
    var isRollScrolling = false
    var isFinisherActive = false

    var targetPosition = 0
    lateinit var currentList: List<CardModel>

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onCreate() {
        currentList = cardsFactory.getCars(10)
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
                    Log.i("Logcat", "scrollDy")
                    _scrollDy.postValue(-10)
                }
        }
    }

    fun startFirstPing() {
        disposableTimeout = observableTimeout.timeout(100L, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _updateVheel.postValue(true)
            }
    }

    fun moveToTarget() {
        isRollScrolling = false
        isFinisherActive = false
        disposable.dispose()
        val startSmoothEmitter = Observable.range(3, 50)
            .concatMap { i: Int ->
                Observable.just(i)
                    .delay(100L, TimeUnit.MILLISECONDS)
                    .map {
                        Log.i("Logcat start", "timer ${i.toDouble().pow(3) / 300}")
//                        Log.i("Logcat start", "timer ${((5.0.pow(2) - (i / 10).toDouble().pow(2))).toLong()}")
                        it
                    }
            }
            .doOnComplete {
                Log.i("Logcat start", "doOnComplete")
                startSmoothDisposable.dispose()
            }
        startSmoothDisposable = startSmoothEmitter
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                Log.i("Logcat start", "doFinally")
                stopSmooth()
            }
            .subscribe {
                Log.i("Logcat start", "emitter ${-(it.toDouble().pow(3) / 300).toInt()}")
                _scrollDy.postValue(-(it.toDouble().pow(3) / 300).toInt())
                Log.i("Logcat", "*************************")
            }

    }

    fun stopSmooth() {
        Log.i("Logcat", "Start stop fun")
        val stopSmoothEmitter = Observable.range(3, 50)
            .concatMap { i: Int ->
                Observable.just(i)
                    .delay(100L, TimeUnit.MILLISECONDS)
                    .map {
                        Log.i("Logcat stop", "timer ${(50 - i).toDouble().pow(3) / 300}")
                        50 - i
                    }
            }
            .doOnComplete {
//                stopSmoothDisposable.dispose()
            }
        stopSmoothDisposable = stopSmoothEmitter
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.i("Logcat start", "emitter ${-(it.toDouble().pow(3) / 300).toInt()}")
                _scrollDy.postValue(-((it).toDouble().pow(3) / 300).toInt())
                Log.i("Logcat", "    ")
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
}