package ca.gustavo.sketchit.di

import ca.gustavo.sketchit.model.RetrofitService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    @Provides
    fun provideKtorClient() = HttpClient(OkHttp) {
        install(WebSockets)
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    @Provides
    fun provideRandomWordRetrofitService(): RetrofitService = Retrofit.Builder()
        .baseUrl("https://random-word-api.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build().create(RetrofitService::class.java)
}