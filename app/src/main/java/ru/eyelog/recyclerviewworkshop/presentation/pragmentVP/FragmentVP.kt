package ru.eyelog.recyclerviewworkshop.presentation.pragmentVP

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.carousel_vp_item.view.vpCarousel
import kotlinx.android.synthetic.main.fragment_main.btTap
import kotlinx.android.synthetic.main.fragment_main.rvCarousel
import kotlinx.android.synthetic.main.fragment_main.vpCarousel00
import kotlinx.android.synthetic.main.fragment_main.vpCarousel01
import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.presentation.pragmentRV.ViewModelRV
import ru.eyelog.recyclerviewworkshop.presentation.pragmentVP.utils.HorizontalMarginSidePageTransformer
import ru.eyelog.recyclerviewworkshop.presentation.pragmentVP.utils.MovePagerTransformer
import ru.eyelog.recyclerviewworkshop.presentation.pragmentVP.utils.applyWindowInsetsOnItem
import javax.inject.Inject

@AndroidEntryPoint
class FragmentVP : Fragment() {

    private val viewModel: ViewModelVP by activityViewModels()

    @Inject
    lateinit var cardAdapter: CardAdapter

    private val snapHelper = PagerSnapHelper()

    private var currentPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        with(vpCarousel00) {
            adapter = cardAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setPageTransformer(HorizontalMarginSidePageTransformer(3))
        }

        with(vpCarousel01) {
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
//        rvCarousel.applyWindowInsetsOnItem()

        viewModel.cardsLiveData.observe(viewLifecycleOwner, {
            cardAdapter.setItem(it)
        })
    }
}