package ca.gustavo.sketchit.ui

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.colorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.gustavo.sketchit.R
import ca.gustavo.sketchit.model.Coordinate

@Composable
fun Draw(vm: MainViewModel = viewModel()) {

    val accent = colorResource(id = R.color.colorAccent)
    val primary = colorResource(id = R.color.colorPrimary)

    val userPath = Path()
    var userPoint by remember { mutableStateOf(Coordinate()) }
    var isUserPainting = false

    val otherUserPath = Path()
    val receivedPoint by vm.getDrawingCoordinates().collectAsState(initial = Coordinate())
    var isOtherUserPainting = false

    Canvas(
        Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        vm.updateDrawing(event.x, event.y)
                        userPoint = Coordinate(event.x, event.y)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        userPoint = Coordinate(event.x, event.y)
                        vm.updateDrawing(event.x, event.y)
                    }
                    MotionEvent.ACTION_UP -> {
                        vm.updateDrawing(-1F, -1F)
                        userPoint = Coordinate(-1F, -1F)
                    }
                    else -> return@pointerInteropFilter false
                }
                return@pointerInteropFilter true
            }
    ) {
        fun drawUserPath() {
            drawPath(
                path = userPath,
                color = accent,
                alpha = 1F,
                style = Stroke(20F, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }

        userPoint.let { point ->
            println("goose: ${point.x}, ${point.y}")
            if (point.x == -1F && point.y == -1F) {
                drawUserPath()
                isUserPainting = false
            } else if (!isUserPainting) {
                isUserPainting = true
                userPath.moveTo(point.x, point.y)
                drawUserPath()
            } else {
                userPath.lineTo(point.x, point.y)
                drawUserPath()
            }
        }

        fun drawOtherPath() {
            drawPath(
                path = otherUserPath,
                color = primary,
                alpha = 1F,
                style = Stroke(20F, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }

        receivedPoint.let { point ->
            if (point.x == -1F && point.y == -1F) {
                drawOtherPath()
                isOtherUserPainting = false
            } else if (!isOtherUserPainting) {
                isOtherUserPainting = true
                otherUserPath.moveTo(point.x, point.y)
                drawOtherPath()
            } else {
                otherUserPath.lineTo(point.x, point.y)
                drawOtherPath()
            }
        }


    }
}