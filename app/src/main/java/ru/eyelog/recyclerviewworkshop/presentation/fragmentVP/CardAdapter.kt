package ru.eyelog.recyclerviewworkshop.presentation.fragmentVP

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.ivIcon
import kotlinx.android.synthetic.main.card_item.view.tvSubTitle
import kotlinx.android.synthetic.main.card_item.view.tvTitle
import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.data.CardModel
import javax.inject.Inject

class CardAdapter @Inject constructor() : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    var list: List<CardModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setItem(list: List<CardModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    class CardViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        constructor(parent: ViewGroup) :
            this(LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false))

        fun bind(cardModel: CardModel) {
            itemView.tvTitle.text = cardModel.title
            itemView.tvSubTitle.text = cardModel.subTitle
            itemView.ivIcon.setImageResource(cardModel.icon)
        }
    }
}