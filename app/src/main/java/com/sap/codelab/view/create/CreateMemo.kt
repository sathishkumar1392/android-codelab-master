package com.sap.codelab.view.create

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sap.codelab.R
import com.sap.codelab.databinding.ActivityCreateMemoBinding
import com.sap.codelab.utils.extensions.empty
import com.sap.codelab.view.detail.ViewMemoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

/**
 * Activity that allows a user to create a new Memo.
 *
 */
internal class CreateMemo : AppCompatActivity() {

    private lateinit var binding: ActivityCreateMemoBinding
    private  val model: CreateMemoViewModel by viewModel()
    private var selectedLatitude: Double? = null
    private var selectedLongitude: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            googleMap.uiSettings.isZoomControlsEnabled = true
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap.isMyLocationEnabled = true

                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

                // Get last known location and zoom directly
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        //  Auto-zoom to current location
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
                        )
                    } else {
                        // fallback if location is null
                        val defaultLatLng = LatLng(52.30, 13.25) // Berlin
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 10f))
                    }
                }
            } else {
                // fallback: move camera to a default location
                val defaultLatLng = LatLng(52.30, 13.25)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 10f))
            }
            googleMap.setOnMapClickListener { latLng ->
                selectedLatitude = latLng.latitude
                selectedLongitude = latLng.longitude
                // Show marker
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng).title("Reminder Location"))
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                model.updateLocation(latLng.latitude, latLng.longitude)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_create_memo, menu)
        return true
    }

    /**
     * Handles actionbar interactions.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveMemo()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Saves the memo if the input is valid; otherwise shows the corresponding error messages.
     */
    private fun saveMemo() {
        binding.contentCreateMemo.run {
            model.updateMemo(
                title = memoTitle.text.toString(),
                description = memoDescription.text.toString(),
                latitude = selectedLatitude ?: 0.0,
                longitude = selectedLongitude ?: 0.0)

            if (model.isMemoValid()) {
                model.saveMemo()
                setResult(RESULT_OK)
                finish()
            } else {
                memoTitleContainer.error = getErrorMessage(model.hasTitleError(), R.string.memo_title_empty_error)
                memoDescription.error = getErrorMessage(model.hasTextError(), R.string.memo_text_empty_error)
            }
        }
    }

    /**
     * Returns the error message if there is an error, or an empty string otherwise.
     *
     * @param hasError          - whether there is an error.
     * @param errorMessageResId - the resource id of the error message to show.
     * @return the error message if there is an error, or an empty string otherwise.
     */
    private fun getErrorMessage(hasError: Boolean, @StringRes errorMessageResId: Int): String {
        return if (hasError) {
            getString(errorMessageResId)
        } else {
            String.empty()
        }
    }
}
