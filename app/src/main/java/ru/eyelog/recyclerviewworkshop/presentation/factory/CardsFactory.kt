package ru.eyelog.recyclerviewworkshop.presentation.factory

import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.data.CardModel
import javax.inject.Inject

class CardsFactory @Inject constructor() {

    fun getCards(cardsNumb: Int): List<CardModel> {
        val outData = mutableListOf<CardModel>()
        for (i in 0..cardsNumb) {
            outData.add(
                CardModel(
                    title = "Title $i",
                    subTitle = "Title $i",
                    icon = R.drawable.ic_menu_list
                )
            )
        }
        return outData
    }
}