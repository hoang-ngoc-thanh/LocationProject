package com.example.locationproject.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.locationproject.MainActivity
import com.example.locationproject.R
import com.example.locationproject.room.LocationInfo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        const val DEFAULT_ZOOM = 15f
    }

    private lateinit var mMap: GoogleMap
    private var isPermissionSuccess: Boolean = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mapViewModel: MapViewModel

    private val defaultLocation = LatLng(-33.8523341, 151.2106085) //default Sydney

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapViewModel = MapViewModel((activity as MainActivity).getDatabase())

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(activity as MainActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isPermissionSuccess = (activity as MainActivity).isPermissionSuccess()
        initObservable()
        initMap()
        mapViewModel.fetchListLocation()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(ggMap: GoogleMap) {
        mMap = ggMap

        mMap.isMyLocationEnabled = isPermissionSuccess
        mMap.uiSettings.isMyLocationButtonEnabled = false

        if (isPermissionSuccess) {
            requestLocation()
        } else
            (activity as MainActivity).requestLocationPermission()

    }

    private fun initObservable() {
        mapViewModel.apply {
            currentLocationLiveData.observe(viewLifecycleOwner) {
                updateCurrentPlace(it)
            }

            previousLocationLiveData.observe(viewLifecycleOwner) {
                it.forEach { locationInfo ->
                    updateCurrentPlace(locationInfo)
                }
            }

            startTrackingLiveData.observe(viewLifecycleOwner) {
                requestLocation()
            }
        }
    }

    private fun initMap() {
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                mapViewModel.saveCurrentLocation(
                    id = mapViewModel.getListLocationTracked().size,
                    location = it
                )
            }
        }
    }

    private fun updateCurrentPlace(location: LocationInfo) {
        with(mMap) {
            val latLng = LatLng(
                location.lat ?: defaultLocation.latitude,
                location.lng ?: defaultLocation.longitude
            )
            addMarker(MarkerOptions().position(latLng).title("I am here"))
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng, DEFAULT_ZOOM
                )
            )
        }
    }
}