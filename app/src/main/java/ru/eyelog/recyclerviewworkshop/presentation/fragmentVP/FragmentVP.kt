package ru.eyelog.recyclerviewworkshop.presentation.fragmentVP

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.PagerSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.rvCarousel
import kotlinx.android.synthetic.main.fragment_main.vpCarousel00
import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.presentation.fragmentVP.utils.MovePagerTransformer
import javax.inject.Inject

@AndroidEntryPoint
class FragmentVP : Fragment() {

    private val viewModel: ViewModelVP by activityViewModels()

    @Inject
    lateinit var cardAdapter: CardAdapter

    private val snapHelper = PagerSnapHelper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

//        with(vpCarousel01) {
//            adapter = cardAdapter
//            setPadding(
//                context.dip2px(32),
//                context.dip2px(0),
//                context.dip2px(32),
//                context.dip2px(0)
//            )
//            clipToPadding = false
//            clipChildren = false
//            offscreenPageLimit = 3
//            setPageTransformer(MovePagerTransformer(3))
//        }

        with(vpCarousel00) {
            adapter = cardAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setPageTransformer(MovePagerTransformer(3))
        }
//
//        btTap.setOnClickListener {
//            if (currentPosition < 5){
//                currentPosition++
//            } else {
//                currentPosition = 0
//            }
//
//            vpCarousel00.setCurrentItem(currentPosition, true)
//        }

//        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
//        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
//        vpCarousel00.setPageTransformer { page, position ->
//            val viewPager = page.parent.parent as ViewPager2
//            val offset = position * -(2 * offsetPx + pageMarginPx)
//            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
//                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
//                    page.translationX = -offset
//                } else {
//                    page.translationX = offset
//                }
//            } else {
//                page.translationY = offset
//            }
//        }

//        vpCarousel00.setPadding(0, 0 ,256, 0)
//        vpCarousel00.clipToPadding = false
//        vpCarousel00.clipChildren = false

//        val marginTransformer = HorizontalMarginSidePageTransformer(6)
//        vpCarousel00.setPageTransformer(marginTransformer)

        rvCarousel.adapter = cardAdapter
        snapHelper.attachToRecyclerView(rvCarousel)
        rvCarousel.visibility = View.GONE

//        rvCarousel.applyWindowInsetsOnItem()

        viewModel.cardsLiveData.observe(viewLifecycleOwner, {
            cardAdapter.setItem(it)
        })
    }
}