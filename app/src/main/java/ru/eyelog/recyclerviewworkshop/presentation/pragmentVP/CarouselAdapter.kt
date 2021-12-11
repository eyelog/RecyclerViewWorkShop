package ru.eyelog.recyclerviewworkshop.presentation.pragmentVP

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.carousel_vp_item.view.vpCarousel
import kotlinx.android.synthetic.main.fragment_main.rvCarousel
import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.data.CardModel
import javax.inject.Inject

class CarouselAdapter @Inject constructor(
    private val cardAdapter: CardAdapter
) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    var list: List<List<CardModel>> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setItem(list: List<List<CardModel>>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    inner class CarouselViewHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        constructor(parent: ViewGroup) :
            this(LayoutInflater.from(parent.context).inflate(R.layout.carousel_vp_item, parent, false))

        fun bind(carousel: List<CardModel>) {

            with(itemView.vpCarousel) {
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3
            }

            itemView.vpCarousel.adapter = cardAdapter
            cardAdapter.setItem(carousel)

            val pageMarginPx = 10
            val offsetPx = 16
            itemView.vpCarousel.setPageTransformer { page, position ->
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * offsetPx + pageMarginPx)
                if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -offset
                    } else {
                        page.translationX = offset
                    }
                } else {
                    page.translationY = offset
                }
            }

        }
    }
}