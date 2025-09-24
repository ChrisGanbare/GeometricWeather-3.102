package wangdaye.com.geometricweather.location.services

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.WorkerThread
import wangdaye.com.geometricweather.common.utils.helpers.LogHelper

// static.

private const val TIMEOUT_MILLIS = (10 * 1000).toLong()

private fun isLocationEnabled(
    manager: LocationManager
): Boolean {
    val enabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        manager.isLocationEnabled
    } else {
        manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    LogHelper.log("AndroidLocationService", "isLocationEnabled: $enabled")
    return enabled
}

private fun getBestProvider(locationManager: LocationManager): String {
    var provider = locationManager.getBestProvider(
        Criteria().apply {
            isBearingRequired = false
            isAltitudeRequired = false
            isSpeedRequired = false
            accuracy = Criteria.ACCURACY_FINE
            horizontalAccuracy = Criteria.ACCURACY_HIGH
            powerRequirement = Criteria.POWER_HIGH
        },
        true
    ) ?: ""

    if (provider.isEmpty()) {
        provider = locationManager
            .getProviders(true)
            .getOrNull(0) ?: provider
    }
    
    LogHelper.log("AndroidLocationService", "getBestProvider: $provider")
    return provider
}

@SuppressLint("MissingPermission")
private fun getLastKnownLocation(
    locationManager: LocationManager
): Location? {
    val networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    val passiveLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
    
    val location = networkLocation ?: gpsLocation ?: passiveLocation
    LogHelper.log("AndroidLocationService", "getLastKnownLocation: ${location?.latitude}, ${location?.longitude}")
    return location
}

// interface.

@SuppressLint("MissingPermission")
open class AndroidLocationService : LocationService(), LocationListener {

    private val timer = Handler(Looper.getMainLooper())

    private var locationManager: LocationManager? = null

    private var currentProvider = ""
    private var locationCallback: LocationCallback? = null
    private var lastKnownLocation: Location? = null
    private var gmsLastKnownLocation: Location? = null

    override fun requestLocation(context: Context, callback: LocationCallback) {
        LogHelper.log("AndroidLocationService", "requestLocation started")
        cancel()

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val hasLocationManager = locationManager != null
        val hasPermission = hasPermissions(context)
        val isLocationEnabled = locationManager?.let { isLocationEnabled(it) } ?: false
        val bestProvider = locationManager?.let { getBestProvider(it) } ?: ""
        currentProvider = bestProvider
        
        LogHelper.log("AndroidLocationService", "hasLocationManager=$hasLocationManager, hasPermission=$hasPermission, isLocationEnabled=$isLocationEnabled, bestProvider=$bestProvider")

        if (locationManager == null
            || !hasPermissions(context)
            || !isLocationEnabled(locationManager!!)
            || bestProvider.isEmpty()) {
            LogHelper.log("AndroidLocationService", "requestLocation failed - returning null")
            callback.onCompleted(null)
            return
        }

        locationCallback = callback
        lastKnownLocation = getLastKnownLocation(locationManager!!)

        locationManager!!.requestLocationUpdates(
            currentProvider,
            0L,
            0F,
            this,
            Looper.getMainLooper()
        )
        timer.postDelayed({
            cancel()
            val location = gmsLastKnownLocation ?: lastKnownLocation
            LogHelper.log("AndroidLocationService", "requestLocation timeout - handling location: ${location?.latitude}, ${location?.longitude}")
            handleLocation(location)
        }, TIMEOUT_MILLIS)
    }

    override fun cancel() {
        LogHelper.log("AndroidLocationService", "cancel called")
        locationManager?.removeUpdates(this)
        timer.removeCallbacksAndMessages(null)
    }

    override val permissions: Array<String>
        get() = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun handleLocation(location: Location?) {
        LogHelper.log("AndroidLocationService", "handleLocation: ${location?.latitude}, ${location?.longitude}")
        locationCallback?.onCompleted(
            location?.let { buildResult(it) }
        )
    }

    @WorkerThread
    private fun buildResult(location: Location): Result {
        LogHelper.log("AndroidLocationService", "buildResult: ${location.latitude.toFloat()}, ${location.longitude.toFloat()}")
        return Result(
            location.latitude.toFloat(),
            location.longitude.toFloat()
        )
    }

    // location listener.

    override fun onLocationChanged(location: Location) {
        LogHelper.log("AndroidLocationService", "onLocationChanged: ${location.latitude}, ${location.longitude}")
        cancel()
        handleLocation(location)
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        LogHelper.log("AndroidLocationService", "onStatusChanged: provider=$provider, status=$status")
        // do nothing.
    }

    override fun onProviderEnabled(provider: String) {
        LogHelper.log("AndroidLocationService", "onProviderEnabled: $provider")
        // do nothing.
    }

    override fun onProviderDisabled(provider: String) {
        LogHelper.log("AndroidLocationService", "onProviderDisabled: $provider")
        // do nothing.
    }
}