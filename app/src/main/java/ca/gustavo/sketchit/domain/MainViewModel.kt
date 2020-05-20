package ca.gustavo.sketchit.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val getDrawingPoints: GetDrawingPointsUseCase,
    private val resetDrawingPoints: ResetDrawingPointsUseCase,
    private val updateDrawingPoints: UpdateDrawingUseCase,
    private val randomWordGenerator: RandomWordGeneratorUseCase,
    private val verifyGuess: VerifyGuessUseCase
) : ViewModel() {

    private val _drawingPoints: MutableLiveData<List<Pair<Float, Float>>> = MutableLiveData()
    val drawingPoints: LiveData<List<Pair<Float, Float>>> = _drawingPoints

    private val _randomWord: MutableLiveData<String> = MutableLiveData()
    val randomWord: LiveData<String> = _randomWord

    private val _isGuessSuccessful: MutableLiveData<Boolean?> = MutableLiveData()
    val isGuessSuccessful: LiveData<Boolean?> = _isGuessSuccessful

    fun startListeningToDrawingPoints() = viewModelScope.launch {
        getDrawingPoints().collect {
            _drawingPoints.postValue(it)
        }
    }

    fun resetDrawing() = viewModelScope.launch {
        resetDrawingPoints()
    }

    fun updateDrawing(x: Float, y: Float) {
        updateDrawingPoints(x, y)
    }

    fun getRandomWord() = viewModelScope.launch {
        val word = randomWordGenerator()
        _randomWord.postValue(word)
    }

    fun checkGuess(guess: String) = viewModelScope.launch {
        _isGuessSuccessful.postValue(verifyGuess(guess))
    }
}