package com.example.vladivostokcityguide.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.example.vladivostokcityguide.domain.models.Point
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class LocationDataSourceImpl(
    private val context: Context
): LocationDataSource {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Point {
        val loc = fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .await()

        return loc
            ?.let { Point(it.latitude, it.longitude) }
            ?: throw IllegalStateException("Current location is unavailable")
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocationFlow(): Flow<Point> {
        return callbackFlow {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        trySend(Point(latitude = location.latitude, longitude = location.longitude))
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    /**
     * Приблизительно оценивает время пешей прогулки и реальное расстояние по дорожной сети
     * между двумя точками с учётом извилистости маршрута.
     *
     * 1) Вычисляет геодезическое расстояние между точками (Haversine formula).
     * 2) Умножает на коэффициент извилистости (detour factor), чтобы учесть
     *    кривизну улиц и необходимость объезжать препятствия.
     * 3) Делит скорректированное расстояние на среднюю скорость ходьбы (1.4 м/с),
     *    переводит в минуты и округляет до целого.
     *
     * @param startLatitude   широта начальной точки, градусы.
     * @param startLongitude  долгота начальной точки, градусы.
     * @param endLatitude     широта конечной точки, градусы.
     * @param endLongitude    долгота конечной точки, градусы.
     * коэффициент извилистости (по умолчанию 1.3, т.е. +30%),
     *                        может варьироваться от ~1.1 (редко) до ~1.5 (очень «змеиные» улочки).
     * @return Pair(timeMinutes, adjustedDistanceMeters):
     *   - timeMinutes — примерное время ходьбы, минуты (округлено).
     *   - adjustedDistanceMeters — скорректированное расстояние по сети, метры (округлено).
     *
     * @see <a href="https://en.wikipedia.org/wiki/Haversine_formula">Haversine formula</a>
     */
    override suspend fun getTimeAndDistance(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
    ): Pair<Int, Int> {
        // 1) Расчёт «по прямой» (geodetic distance)
        val R = 6_371_000.0  // средний радиус Земли, м

        val lat1 = Math.toRadians(startLatitude)
        val lon1 = Math.toRadians(startLongitude)
        val lat2 = Math.toRadians(endLatitude)
        val lon2 = Math.toRadians(endLongitude)
        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2).pow(2) +
                cos(lat1) * cos(lat2) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val straightDistance = R * c

        // 2) Скорректировать для «извилистости» улиц
        val detourFactor = 1.3
        val adjustedDistance = straightDistance * detourFactor
        val distanceMeters = adjustedDistance.roundToInt()

        // 3) Оценить время пешей ходьбы
        val walkingSpeedMps = 1.4  // м/с (~5 км/ч)
        val timeSeconds = distanceMeters / walkingSpeedMps
        val timeMinutes = (timeSeconds / 60).roundToInt()

        return timeMinutes to distanceMeters
    }

}