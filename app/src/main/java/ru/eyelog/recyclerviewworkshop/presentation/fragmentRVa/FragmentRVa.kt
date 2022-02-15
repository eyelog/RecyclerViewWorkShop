package ru.eyelog.recyclerviewworkshop.presentation.fragmentRVa

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
import ru.eyelog.recyclerviewworkshop.presentation.fragmentRV.localutils.SnapListenerBehavior
import ru.eyelog.recyclerviewworkshop.presentation.fragmentRV.localutils.SnapOnScrollListener
import javax.inject.Inject

@AndroidEntryPoint
class FragmentRVa : Fragment() {

    private val viewModel: ViewModelRVa by activityViewModels()

    private val mLayoutManager = CenterZoomLayoutManagerRVa(context)
    private val snapHelper = LinearSnapHelper()
    private val itemScrollPosition = { position: Int ->
        viewModel.setCurrentPosition(position)
    }
    private val snapOnScrollListener = SnapOnScrollListener(
        snapHelper,
        SnapListenerBehavior.NOTIFY_ON_SCROLL,
        itemScrollPosition
    )

    @Inject
    lateinit var cardAdapter: VerticalCardAdapterRVa

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
//            Log.i("Logcat", "position ${rvCarousel.get}")
        })

        viewModel.updateVheel.observe(viewLifecycleOwner, {
            mLayoutManager.updateItemsFit()
        })

        viewModel.setTargetPosition.observe(viewLifecycleOwner, {
            buttonScroll.text = "Крутить до $it"
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

//            val ss = LinearSmoothScroller(context)
//            ss.targetPosition = Int.MAX_VALUE/ - 10
//            (rvCarousel.layoutManager as CenterZoomLayoutManagerRVc).startSmoothScroll(ss)
    }
}