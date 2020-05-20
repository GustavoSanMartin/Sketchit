package ca.gustavo.sketchit.di

import ca.gustavo.sketchit.model.RetrofitService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    @Provides
    fun provideRandomWordRetrofitService(): RetrofitService = Retrofit.Builder()
        .baseUrl("https://random-word-api.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build().create(RetrofitService::class.java)

    @Provides
    fun providesFirestore(): FirebaseFirestore = Firebase.firestore
}