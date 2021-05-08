package ca.gustavo.sketchit.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.databinding.FragmentDrawingBinding
import ca.gustavo.sketchit.di.injector
import ca.gustavo.sketchit.domain.MainViewModel
import ca.gustavo.sketchit.model.Coordinate
import ca.gustavo.sketchit.util.viewBinding
import kotlinx.coroutines.flow.collect

class DrawingFragment : Fragment(R.layout.fragment_drawing) {

    private val viewModel by viewModels<MainViewModel> { injector.mainViewModelFactory() }
    private val binding by viewBinding(FragmentDrawingBinding::bind)

    private lateinit var drawingView: DrawingView
    private var isDrawing = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        drawingView = binding.drawingView

        lifecycleScope.launchWhenStarted {
            viewModel.getDrawingCoordinates().collect {
                onReceiveSnapshot(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.getPainterNames().collect {
                binding.prompt.text = "${it.size} painters connected"
            }
        }

        drawingView.setOnDrawListener(object : DrawingListener {
            override fun onStartDraw(x: Float, y: Float) {
                viewModel.updateDrawing(x, y)
            }

            override fun onDraw(x: Float, y: Float) {
                viewModel.updateDrawing(x, y)
            }

            override fun onEndDraw() {
                viewModel.updateDrawing(-1F, -1F)
            }
        })
    }

    private fun onReceiveSnapshot(point: Coordinate) {
        println(point)
        if (point.x == -1F && point.y == -1F) {
            isDrawing = false
            requireActivity().runOnUiThread {
                drawingView.endDraw()
            }
        } else if (!isDrawing) {
            isDrawing = true
            requireActivity().runOnUiThread {
                drawingView.startDraw(point.x, point.y)
            }
        } else {
            requireActivity().runOnUiThread {
                drawingView.draw(point.x, point.y)
            }
        }
    }
}