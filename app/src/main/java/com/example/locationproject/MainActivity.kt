package com.example.locationproject

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.locationproject.databinding.ActivityMainBinding
import com.example.locationproject.history.HistoryFragment
import com.example.locationproject.map.MapFragment
import com.example.locationproject.room.AppDatabase

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101
    }

    private lateinit var viewBinding: ActivityMainBinding
    private var locationPermissionGranted = false
    private lateinit var appDatabase : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initDataBase()
        requestLocationPermission()
        layoutInit()
        handleListeners()
    }

    private fun addFragment(fragment: Fragment, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.flContainer, fragment)
        transaction.addToBackStack(tag)
        transaction.commit()
    }

    private fun initDataBase(){
        appDatabase = AppDatabase.getDatabase(this)
    }

    fun getDatabase() = appDatabase

    fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            addFragment(MapFragment(), "A")
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    private fun layoutInit() {
        viewBinding.btnLocation.isActivated = true
    }

    private fun handleListeners() {
        viewBinding.apply {
            btnLocation.setOnClickListener {
                btnLocation.isActivated = true
                btnHistory.isActivated = false
                if (supportFragmentManager.fragments.filterIsInstance<MapFragment>().isNotEmpty()) {
                    supportFragmentManager.popBackStack("B", POP_BACK_STACK_INCLUSIVE)
                } else {
                    addFragment(MapFragment(), "A")
                }
            }

            btnHistory.setOnClickListener {
                btnLocation.isActivated = false
                btnHistory.isActivated = true
                addFragment(HistoryFragment(), "B")
            }
        }
    }

    fun isPermissionSuccess() = locationPermissionGranted

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                    addFragment(MapFragment(), "A")
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

