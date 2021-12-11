package ru.eyelog.recyclerviewworkshop.presentation.pragmentVP

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.rvCarousel
import ru.eyelog.recyclerviewworkshop.R
import ru.eyelog.recyclerviewworkshop.presentation.pragmentRV.ViewModelRV
import javax.inject.Inject

@AndroidEntryPoint
class FragmentVP : Fragment() {

    private val viewModel: ViewModelVP by activityViewModels()

    @Inject
    lateinit var carouselAdapter: CarouselAdapter

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

        rvCarousel.adapter = carouselAdapter

        viewModel.cardsLiveData.observe(viewLifecycleOwner, {
            carouselAdapter.setItem(it)
        })
    }
}