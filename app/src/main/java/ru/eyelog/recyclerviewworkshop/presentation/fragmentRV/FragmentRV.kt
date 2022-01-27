package ru.eyelog.recyclerviewworkshop.presentation.fragmentRV

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.rvCarousel
import kotlinx.android.synthetic.main.fragment_vertical.buttonScroll
import ru.eyelog.recyclerviewworkshop.R
import javax.inject.Inject

@AndroidEntryPoint
class FragmentRV : Fragment() {

    private val viewModel: ViewModelRV by activityViewModels()

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

        val mLayoutManager = LinearLayoutManager(context)
        rvCarousel.layoutManager = mLayoutManager

        rvCarousel.adapter = cardAdapter

        viewModel.cardsLiveData.observe(viewLifecycleOwner, {
            cardAdapter.setItem(it)
        })

        buttonScroll.setOnClickListener {
            rvCarousel.smoothScrollToPosition(150)
        }
    }
}