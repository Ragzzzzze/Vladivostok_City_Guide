package com.example.vladivostokcityguide.di

import androidx.room.Room
import com.example.vladivostokcityguide.data.local.CityGuideDatabase
import com.example.vladivostokcityguide.data.location.LocationDataSource
import com.example.vladivostokcityguide.data.location.LocationDataSourceImpl
import com.example.vladivostokcityguide.data.network.KtorRemoteDataSource
import com.example.vladivostokcityguide.data.network.RemoteDataSource
import com.example.vladivostokcityguide.data.repository.CityGuideRepositoryImpl
import com.example.vladivostokcityguide.domain.CityGuideRepository
import com.example.vladivostokcityguide.presentation.MapScreen.MapScreenViewModel
import com.example.vladivostokcityguide.presentation.PlacesScreen.PlacesScreenViewModel
import com.example.vladivostokcityguide.presentation.favorite_screen.FavoriteScreenViewModel
import com.example.vladivostokcityguide.presentation.landmark_details_screen.LandmarkDetailsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRedirect
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val GuideAppModule = module {
    single(named("GoogleDriveClient")) {
        HttpClient(OkHttp) {
            // Эта настройка позволит клиенту автоматически следовать перенаправлениям от сервера.
            // Google Drive использует это для предоставления прямой ссылки на скачивание.
            install(HttpRedirect) {
                checkHttpMethod = false
                allowHttpsDowngrade = false
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    },
                    contentType = ContentType.Any
                )
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "www.googleapis.com"
                }
                header("Accept", "application/json")
            }
            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.BODY
            }
        }
    }
    single(named("graphHopperClient")) {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "graphhopper.com"
                    encodedPath = "api/1/route"
                }
            }
            install(Logging) { level = LogLevel.BODY }
        }
    }
    single<RemoteDataSource> {
        KtorRemoteDataSource(
            client = get(named("GoogleDriveClient")),
            graphHopperClient = get(named("graphHopperClient"))
        )
    }
    single<LocationDataSource> {
        LocationDataSourceImpl(androidContext())
    }

    single<CityGuideDatabase> {
        Room.databaseBuilder(
            get(),
            CityGuideDatabase::class.java,
            "city_guide_database.db"
        ).build()
    }

    single { get<CityGuideDatabase>().cityGuideDao }
    singleOf(::CityGuideRepositoryImpl).bind<CityGuideRepository>()
    viewModelOf(::PlacesScreenViewModel)
    viewModelOf(::LandmarkDetailsViewModel)
    viewModelOf(::MapScreenViewModel)
    viewModelOf(::FavoriteScreenViewModel)
}