package ru.eyelog.recyclerviewworkshop.presentation.fragmentShop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_scale_shop.activation_layout
import kotlinx.android.synthetic.main.fragment_scale_shop.seekBar
import kotlinx.android.synthetic.main.fragment_scale_shop.viewSap00
import kotlinx.android.synthetic.main.fragment_scale_shop.viewSap01
import kotlinx.android.synthetic.main.fragment_scale_shop.viewSap02
import kotlinx.android.synthetic.main.fragment_scale_shop.viewSap03
import ru.eyelog.recyclerviewworkshop.R

@AndroidEntryPoint
class FragmentScaleShop: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scale_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
//                viewSap00.scaleX = seekBar.progress.toFloat() / 100f
//                viewSap00.scaleY = seekBar.progress.toFloat() / 100f
                resize(viewSap00, seekBar.progress.toFloat(), seekBar.progress.toFloat())
                viewSap01.scaleX = seekBar.progress.toFloat() / 100f
                viewSap01.scaleY = seekBar.progress.toFloat() / 100f
                viewSap02.scaleX = seekBar.progress.toFloat() / 100f
                viewSap02.scaleY = seekBar.progress.toFloat() / 100f
                viewSap03.scaleX = seekBar.progress.toFloat() / 100f
                viewSap03.scaleY = seekBar.progress.toFloat() / 100f
                activation_layout.scaleX = seekBar.progress.toFloat() / 100f
                activation_layout.scaleY = seekBar.progress.toFloat() / 100f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun resize(view: View, scaleX: Float, scaleY: Float) {
        val layoutParams = view.layoutParams
        layoutParams.width = (scaleX*3).toInt()
        layoutParams.height = (scaleY*3).toInt()
        view.layoutParams = layoutParams
    }
}