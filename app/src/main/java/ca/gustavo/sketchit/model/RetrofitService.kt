package ca.gustavo.sketchit.model

import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("/word")
    suspend fun getRandomWords(@Query("number") numWords: Int = 1): List<String>
}