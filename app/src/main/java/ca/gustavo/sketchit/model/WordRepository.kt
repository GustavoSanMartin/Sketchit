package ca.gustavo.sketchit.model

import javax.inject.Inject

class WordRepository @Inject constructor(private val webservice: RetrofitService) {
    suspend fun getRandomWords(numWords: Int = 1) = webservice.getRandomWords(numWords)
}