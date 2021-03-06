package ru.eyelog.recyclerviewworkshop.presentation.fragmentRVc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_vertical.buttonScroll
import kotlinx.android.synthetic.main.fragment_vertical_with_tapper.rvCarousel
import ru.eyelog.recyclerviewworkshop.R
import javax.inject.Inject

@AndroidEntryPoint
class FragmentRVc : Fragment() {

    private val viewModel: ViewModelRVc by activityViewModels()

    private val mLayoutManager = CenterZoomLayoutManagerRVc(context)
    private val snapHelper = LinearSnapHelper()

    var shouldControlTraffic = false
    var firstVisibleItemPosition = 0

    @Inject
    lateinit var cardAdapter: VerticalCardAdapterRVc

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vertical_with_tapper, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        rvCarousel.layoutManager = mLayoutManager
        rvCarousel.adapter = cardAdapter

        viewModel.cardsLiveData.observe(viewLifecycleOwner, {
            cardAdapter.setItem(it)
            rvCarousel.scrollToPosition(((Int.MAX_VALUE / 2) / it.size) * it.size - 2)
            viewModel.startScrolling()
//            viewModel.startFirstPing()
        })

        viewModel.scrollPosition.observe(viewLifecycleOwner, {
            scrollToPosition(it)
        })

        viewModel.scrollDy.observe(viewLifecycleOwner, {
            firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()
            scrollToPositionByDx(it)
        })

        viewModel.updateVheel.observe(viewLifecycleOwner, {
            mLayoutManager.updateItemsFit()
        })

        viewModel.setTargetPosition.observe(viewLifecycleOwner, {
            buttonScroll.text = "?????????????? ???? $it"
        })

        viewModel.finishController.observe(viewLifecycleOwner, {
            if (!shouldControlTraffic) {
                shouldControlTraffic = true
            }
            val targetVisiblePosition = firstVisibleItemPosition - it.first + 2
            if (targetVisiblePosition % it.second == 0) {
                attachSnapHelper()
                viewModel.stopFinisher()
            }
        })

        buttonScroll.setOnClickListener {
            val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()
            viewModel.moveToTarget(cardAdapter.getItemHeight(), firstVisibleItemPosition)
        }
    }

    private fun attachSnapHelper(){
        snapHelper.attachToRecyclerView(rvCarousel)
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