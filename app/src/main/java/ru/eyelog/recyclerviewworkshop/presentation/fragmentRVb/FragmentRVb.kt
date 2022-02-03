package ru.eyelog.recyclerviewworkshop.presentation.fragmentRVb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_vertical.rvCarousel
import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.presentation.fragmentRVb.lib.LoopingLayoutManager
import javax.inject.Inject

@AndroidEntryPoint
class FragmentRVb : Fragment() {

    private val viewModel: ViewModelRVb by activityViewModels()
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private var snapHelper: SnapHelper? = null

    @Inject
    lateinit var cardAdapter: RVbCardAdapter

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

        mLayoutManager = LoopingLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rvCarousel.layoutManager = mLayoutManager
        rvCarousel.adapter = cardAdapter
        snapHelper?.attachToRecyclerView(rvCarousel)

        viewModel.cardsLiveData.observe(viewLifecycleOwner, {
            cardAdapter.setItem(it)
        })
    }
}