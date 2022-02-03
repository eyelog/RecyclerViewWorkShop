package ru.eyelog.recyclerviewworkshop.presentation.fragmentRV

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_vertical.buttonScroll
import kotlinx.android.synthetic.main.fragment_vertical.rvCarousel
import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.presentation.fragmentRV.localutils.HideLastDecorator
import ru.eyelog.recyclerviewworkshop.presentation.fragmentRV.localutils.SnapListenerBehavior
import ru.eyelog.recyclerviewworkshop.presentation.fragmentRV.localutils.SnapOnScrollListener
import javax.inject.Inject

@AndroidEntryPoint
class FragmentRV : Fragment() {

    private val viewModel: ViewModelRV by activityViewModels()

    //    private val mLayoutManager = LinearLayoutManager(context)
    private val mLayoutManager = CenterZoomLayoutManager(context)
    private val snapHelper = LinearSnapHelper()
    private val itemScrollPosition = { position: Int ->
        // will show the prize
    }
    private val snapOnScrollListener = SnapOnScrollListener(
        snapHelper,
        SnapListenerBehavior.NOTIFY_ON_SCROLL_STATE_IDLE,
        itemScrollPosition
    )

    @Inject
    lateinit var cardAdapter: VerticalCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vertical, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        rvCarousel.layoutManager = mLayoutManager
        rvCarousel.addOnScrollListener(snapOnScrollListener)
        snapHelper.attachToRecyclerView(rvCarousel)
        rvCarousel.adapter = cardAdapter
//        rvCarousel.addItemDecoration(HideLastDecorator())

        viewModel.cardsLiveData.observe(viewLifecycleOwner, {
            cardAdapter.setItem(it)
            rvCarousel.scrollToPosition(Int.MAX_VALUE / 2)
            viewModel.startScrolling()
//            viewModel.startFirstPing()
        })

        viewModel.scrollPosition.observe(viewLifecycleOwner, {
            scrollToPosition(it)
        })

        viewModel.scrollDy.observe(viewLifecycleOwner, {
            scrollToPositionByDx(it)
        })

        viewModel.updateVheel.observe(viewLifecycleOwner, {
            mLayoutManager.updateItemsFit()
        })

        buttonScroll.setOnClickListener {
            viewModel.moveToTarget()
        }
    }

    private fun scrollToPosition(position: Int) {
        rvCarousel.smoothScrollToPosition(position)
    }

    private fun scrollToPositionByDx(i: Int) {
        rvCarousel.smoothScrollBy(0, i)
        viewModel.setCurrentPosition(mLayoutManager.findFirstVisibleItemPosition())
        mLayoutManager.findFirstCompletelyVisibleItemPosition()
    }
}