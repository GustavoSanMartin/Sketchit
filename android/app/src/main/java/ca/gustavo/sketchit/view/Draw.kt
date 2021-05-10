package ca.gustavo.sketchit.view

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.domain.MainViewModel
import ca.gustavo.sketchit.model.Coordinate

@Composable
fun Draw(vm: MainViewModel = viewModel()) {
    var isDrawing = false
    var x: Float
    var y: Float

    val accent = colorResource(id = R.color.colorAccent)

    val paint = Paint().apply {
        isAntiAlias = true
        color = accent
        strokeWidth = 20F
        style = PaintingStyle.Stroke
        strokeCap = StrokeCap.Round
        strokeJoin = StrokeJoin.Round
    }

    val path = Path()
    var coordinate by remember { mutableStateOf(Coordinate()) }

    Canvas(
        Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN,  -> {
                        x = event.x
                        y = event.y
                        coordinate = Coordinate(x, y)
                        println("goose: down: $x, $y")
                        path.moveTo(x, y)
                        vm.updateDrawing(x, y)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        x = event.x
                        y = event.y
                        coordinate = Coordinate(x, y)
                        path.lineTo(x, y)
                        println("goose: move: $x, $y")
                        vm.updateDrawing(x, y)
                    }
                    MotionEvent.ACTION_UP -> {
                        coordinate = Coordinate(-1F, -1F)
                        vm.updateDrawing(-1F, -1F)

                        path.reset()
                    }
                }

                true
            }

    ) {
        println("goose: drawing")
        drawPath(path, accent)
//        drawIntoCanvas {
//            P
//            if (coordinate.isNegative()) {
//                isDrawing = false
//            }
//
//        }
    }
}