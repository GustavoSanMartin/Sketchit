package ca.gustavo.sketchit.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.gustavo.sketchit.model.Coordinate
import kotlinx.coroutines.flow.collect
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

        viewModelScope.launch {
            getDrawingPoints().collect { _drawingPoints.postValue(it) }
        }

        viewModelScope.launch {
            getNames().collect { _names.postValue(it) }
        }
    }

    private val _drawingPoints: MutableLiveData<Coordinate> = MutableLiveData()
    val drawingPoints: LiveData<Coordinate> = _drawingPoints

    private val _names: MutableLiveData<List<String>> = MutableLiveData()
    val names: LiveData<List<String>> = _names

    private val _isGuessSuccessful: MutableLiveData<Boolean?> = MutableLiveData()
    val isGuessSuccessful: LiveData<Boolean?> = _isGuessSuccessful

    fun sendName(name: String) = viewModelScope.launch {
        sendMyName(name)
    }

    fun updateDrawing(x: Float, y: Float) {
        updateDrawingPoints(x, y)
    }
}