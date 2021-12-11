package ru.eyelog.recyclerviewworkshop.presentation.pragmentVP

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.eyelog.recyclerviewworkshop.data.CardModel
import ru.eyelog.recyclerviewworkshop.presentation.factory.CardsFactory
import javax.inject.Inject

@HiltViewModel
class ViewModelVP @Inject constructor(
    private val cardsFactory: CardsFactory
) : ViewModel(), LifecycleObserver {

    val cardsLiveData: LiveData<List<List<CardModel>>> get() = _cardsLiveData
    private val _cardsLiveData = MediatorLiveData<List<List<CardModel>>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        _cardsLiveData.postValue(cardsFactory.getCars(12, 5))
    }
}