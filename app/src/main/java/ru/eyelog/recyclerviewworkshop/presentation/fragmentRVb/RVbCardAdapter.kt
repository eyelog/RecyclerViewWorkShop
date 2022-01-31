package ru.eyelog.recyclerviewworkshop.presentation.fragmentRVb

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

class RVbCardAdapter @Inject constructor() : RecyclerView.Adapter<RVbCardAdapter.RVbCardViewHolder>() {

    var list: List<CardModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVbCardViewHolder {
        return RVbCardViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RVbCardViewHolder, position: Int) {
        if (list.isEmpty()){
            holder.bind(null)
        } else {
            holder.bind(list[position % list.size])
        }
    }

    fun setItem(list: List<CardModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = Int.MAX_VALUE

    class RVbCardViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        constructor(parent: ViewGroup) :
            this(LayoutInflater.from(parent.context).inflate(R.layout.vertical_card_item, parent, false))

        fun bind(cardModel: CardModel?) {
            cardModel?.let {
                itemView.tvTitle.text = cardModel.title
                itemView.tvSubTitle.text = cardModel.subTitle
                itemView.ivIcon.setImageResource(cardModel.icon)
            }
        }
    }
}
