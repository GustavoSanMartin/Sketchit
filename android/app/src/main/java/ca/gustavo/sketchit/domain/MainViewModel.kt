package ca.gustavo.sketchit.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(
    private val connectToWebsocket: ConnectToWebsocketUseCase,
    private val getDrawingPoints: GetDrawingPointsUseCase,
    private val updateDrawingPoints: UpdateDrawingUseCase,
    private val sendMyName: SendNameUseCase,
    private val getNames: GetNamesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            connectToWebsocket()
        }
    }

    fun getPainterNames() = getNames()
    fun getDrawingCoordinates() = getDrawingPoints()

    fun sendName(name: String) = viewModelScope.launch {
        sendMyName(name)
    }

    fun updateDrawing(x: Float, y: Float) {
        updateDrawingPoints(x, y)
    }
}