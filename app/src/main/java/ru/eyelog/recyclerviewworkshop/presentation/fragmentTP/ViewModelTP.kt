package ru.eyelog.recyclerviewworkshop.presentation.fragmentTP

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.eyelog.recyclerviewworkshop.presentation.factory.CardsFactory
import javax.inject.Inject

@HiltViewModel
class ViewModelTP @Inject constructor(
    private val cardsFactory: CardsFactory
) : ViewModel(), LifecycleObserver {

}