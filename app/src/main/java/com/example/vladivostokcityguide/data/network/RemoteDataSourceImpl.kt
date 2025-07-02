package com.example.vladivostokcityguide.data.network


import com.example.vladivostokcityguide.data.dto.LandmarkDto
import com.example.vladivostokcityguide.data.dto.route.RouteDto
import com.example.vladivostokcityguide.domain.PlaceCategory
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.encodedPath

class KtorRemoteDataSource(
    private val client: io.ktor.client.HttpClient,
    private val graphHopperClient: io.ktor.client.HttpClient
): RemoteDataSource {
    override suspend fun getPlacesByCategory(placeCategory: PlaceCategory): List<LandmarkDto> {
        val fileId = when (placeCategory) {
            PlaceCategory.THEATER -> "1WiEC3t6F3TvNvyTRjF7R5wXl_YFAqQ72"
            PlaceCategory.STATUE -> {
                "1c2c-f7QcruSGtkDeW6ZHeYmWea7PXzX6"
            }
            PlaceCategory.MUSEUM -> {
                "1pEJ4i_RqL2w-FG9t8hYH6XglPlmbK-l8"
            }
            PlaceCategory.OTHER -> TODO("Для прочих пока ничего нет :(")
        }
        val response = client.get {
            url {
                encodedPath = "/drive/v3/files/$fileId"
                parameters.append("alt", "media")
                parameters.append("key", "AIzaSyB35yUiHfDqfL0nAhklKhKI1l78bf2BDkY")
            }
        }
        return response.body<List<LandmarkDto>>()
    }

    override suspend fun getTimeAndDistance(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double
    ): RouteDto? {
        val apiKey = "4262ae17-a0c1-41b3-8cd1-a2af69c0b442"
        val response = graphHopperClient.get {
            parameter("point", "$startLatitude,$startLongitude")
            parameter("point", "$endLatitude,$endLongitude")
            parameter("vehicle", "foot")
            parameter("weighting", "fastest")
            parameter("locale", "ru")
            parameter("points_encoded", "false")
            parameter("key", apiKey)
        }
        return response.body<RouteDto>()
    }
}