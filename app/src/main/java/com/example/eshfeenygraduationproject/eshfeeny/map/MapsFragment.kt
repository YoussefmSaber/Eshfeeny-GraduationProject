package com.example.eshfeenygraduationproject.eshfeeny.map

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.data.repository.PharmacyRepoImpl
import com.example.domain.entity.pharmacyResponse.PharmacyResponseItem
import com.example.eshfeenygraduationproject.R
import com.example.eshfeenygraduationproject.databinding.FragmentMapsBinding
import com.example.eshfeenygraduationproject.eshfeeny.map.bottomSheet.PharmacyBottomSheetFragment
import com.example.eshfeenygraduationproject.eshfeeny.publicViewModel.viewModel.PharmacyViewModel
import com.example.eshfeenygraduationproject.eshfeeny.publicViewModel.viewModelFactory.PharmacyViewModelFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class MapsFragment : Fragment() {

    private var binding: FragmentMapsBinding? = null
    private val requestLocationPermission = 1
    private lateinit var googleMap: GoogleMap // declare the variable for the GoogleMap object
    private lateinit var viewModel: PharmacyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater)

        val repo = PharmacyRepoImpl()
        val viewModelFactory = PharmacyViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory)[PharmacyViewModel::class.java]

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onResume() {
        super.onResume()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private val markers = mutableListOf<Marker>()

    private val callback = OnMapReadyCallback { map ->
        googleMap = map // assign the map to the variable

        viewModel.getAllPharmacies()
        viewModel.allPharmacies.observe(viewLifecycleOwner) { pharmacyResponse ->
            // Clear all the markers from the map
            markers.forEach { it.remove() }
            markers.clear()

            pharmacyResponse.forEach { pharmacy ->
                Log.d("pharmacy data", pharmacy.name)
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(pharmacy.geoLocation.lat, pharmacy.geoLocation.lng))
                        .title(pharmacy.name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pharmacy_pin))
                )
                marker?.tag = pharmacy // Attach pharmacy data to marker tag
                if (marker != null) {
                    markers.add(marker)
                } // Add the marker to the list of markers

                googleMap.setOnMarkerClickListener { clickedMarker ->
                    // Retrieve pharmacy data from clicked marker tag
                    val clickedPharmacy = clickedMarker.tag as? PharmacyResponseItem
                    if (clickedPharmacy != null) {
                        val bottomSheet = PharmacyBottomSheetFragment(clickedPharmacy)

                        bottomSheet.show(childFragmentManager, "PharmacyBottomSheetFragment")
                        true // Consume the event
                    } else {
                        false // Event not consumed
                    }
                }
            }
        }
        setMapStyle(googleMap)
        enableMyLocation(googleMap)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun enableMyLocation(map: GoogleMap) {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                requestLocationPermission
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestLocationPermission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable my location
                enableMyLocation(googleMap)
            } else {
                // Permission denied, show snackbar
                Snackbar.make(
                    requireView(),
                    "Location permission denied",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e("Map", "Can't find the style")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("Map", "can't find style. Error: $e")
        }
    }
}