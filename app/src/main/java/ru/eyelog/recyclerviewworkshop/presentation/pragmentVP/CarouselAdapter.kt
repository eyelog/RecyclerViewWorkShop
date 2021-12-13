package ru.eyelog.recyclerviewworkshop.presentation.pragmentVP

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carousel_vp_item.view.vpCarousel
import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.data.CardModel
import ru.eyelog.recyclerviewworkshop.presentation.pragmentVP.utils.HorizontalMarginSidePageTransformer
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
            val carouselViewPager = itemView.vpCarousel
            carouselViewPager.adapter = cardAdapter
            cardAdapter.setItem(carousel)
            itemView.vpCarousel.setPadding(32, 0, 128, 0)
            itemView.vpCarousel.clipToPadding = false
            itemView.vpCarousel.clipChildren = false

            val marginTransformer = HorizontalMarginSidePageTransformer(6)
            itemView.vpCarousel.setPageTransformer(marginTransformer)

//            with(carouselViewPager) {
//                clipToPadding = false
//                clipChildren = false
//                offscreenPageLimit = 3
//                addItemDecoration(OffsetItemDecorator())
//            }

//            val marginTransformer = HorizontalMarginSidePageTransformer(
//                16,
//                8,
//                8,
//                carousel.size
//            )
//            carouselViewPager.setPageTransformer(marginTransformer)

//            carouselViewPager.setPageTransformer(MarginPageTransformer(16))

//            val pageMarginPx = itemView.resources.getDimensionPixelOffset(R.dimen.pageMargin)
//            val offsetPx = itemView.resources.getDimensionPixelOffset(R.dimen.offset)
//            carouselViewPager.setPageTransformer { page, position ->
//                Log.i("Logcat", "position $position")
//                val viewPager = page.parent.parent as ViewPager2
//                val offset = position * -(2 * offsetPx + pageMarginPx)
//                if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
//                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
//                        page.translationX = -offset
//                    } else {
//                        page.translationX = offset
//                    }
//                } else {
//                    page.translationY = offset
//                }
//            }

        }
    }
}