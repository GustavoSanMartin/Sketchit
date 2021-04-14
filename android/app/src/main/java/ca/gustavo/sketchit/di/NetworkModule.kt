package ca.gustavo.sketchit.di

import dagger.Module
import dagger.Provides
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*

@Module
class NetworkModule {
    @Provides
    fun provideKtorClient() = HttpClient(OkHttp) {
        install(WebSockets)
        install(JsonFeature) { serializer = KotlinxSerializer() }
    }
}