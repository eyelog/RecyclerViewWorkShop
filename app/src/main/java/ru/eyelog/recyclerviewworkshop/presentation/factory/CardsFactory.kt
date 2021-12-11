package ru.eyelog.recyclerviewworkshop.presentation.factory

import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.data.CardModel
import javax.inject.Inject
import kotlin.random.Random

class CardsFactory @Inject constructor() {

    fun getCars(carouselNumb: Int, cardsNumb: Int): List<List<CardModel>> {
        var collectData = mutableListOf<CardModel>()
        val outData = mutableListOf<List<CardModel>>()

        repeat (carouselNumb) {
            repeat(cardsNumb){
                collectData.add(
                    CardModel(
                        title = "Title ${Random.nextInt(10)}",
                        subTitle = "Title ${Random.nextInt(10)}",
                        icon = R.drawable.ic_menu_list
                    )
                )
            }
            outData.add(collectData)
            collectData = mutableListOf()
        }

        return outData
    }
}