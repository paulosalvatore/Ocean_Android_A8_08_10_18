package br.com.paulosalvatore.ocean_android_a8_08_10_18

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

	companion object {
		val TAG_MAPA = "MAPA"
		val PERMISSAO_LOCALIZACAO = 1
	}

	lateinit var mapa: GoogleMap
	lateinit var ultimaLocalizacao: Location

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		inicializarLocalizacao()
	}

	private fun inicializarLocalizacao() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
			&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(
					this,
					arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.ACCESS_COARSE_LOCATION),
					PERMISSAO_LOCALIZACAO
			)

			return
		}

		val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

		val locationProvider = LocationManager.NETWORK_PROVIDER
		// LocationManager.GPS_PROVIDER

		var bestLocation: Location? = null

		for (provider in locationManager.allProviders) {
			val checkLocation: Location? = locationManager.getLastKnownLocation(provider)

			checkLocation?.let {
				if (bestLocation == null || bestLocation!!.accuracy < it.accuracy) {
					bestLocation = it
				}
			}
		}

		val location: Location? = locationManager.getLastKnownLocation(locationProvider)

		location?.let {
			ultimaLocalizacao = it

			locationManager.requestLocationUpdates(
					locationProvider,
					1L,
					2f,
					this
			)

			inicializarMapa()

			atualizarPosicao(it)
		}
	}

	private fun inicializarMapa() {
		val mapFragment = supportFragmentManager.findFragmentById(R.id.fragMapa) as SupportMapFragment
		mapFragment.getMapAsync(this)
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		when (requestCode) {
			PERMISSAO_LOCALIZACAO -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				inicializarLocalizacao()
			}
		}
	}

	override fun onMapReady(googleMap: GoogleMap) {
		Log.d(TAG_MAPA, "$googleMap")

		mapa = googleMap

		val latLng = LatLng(
				ultimaLocalizacao.latitude,
				ultimaLocalizacao.longitude
		)

		mapa.addMarker(
				MarkerOptions()
						.position(latLng)
						.title("Minha posição.")
		)

		mapa.moveCamera(
				CameraUpdateFactory.newLatLngZoom(
						latLng,
						14f
				)
		)
	}

	override fun onLocationChanged(location: Location) {
		Log.d(TAG_MAPA, "Localização alterada.")

		atualizarPosicao(location)
	}

	private fun atualizarPosicao(location: Location) {
		val posicao = Posicao(
				location.latitude,
				location.longitude,
				Calendar.getInstance().time.toString()
		)

		ultimaLocalizacao = location

		val latLng = LatLng(
				ultimaLocalizacao.latitude,
				ultimaLocalizacao.longitude
		)

		if (::mapa.isInitialized) {
			mapa.animateCamera(
					CameraUpdateFactory.newLatLngZoom(
							latLng,
							14f
					)
			)
		}
	}

	override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
		Log.d(TAG_MAPA, "Iniciando busca da localização...")
		Toast.makeText(this, "Iniciando busca da localização...", Toast.LENGTH_LONG).show();
	}

	override fun onProviderEnabled(provider: String?) {
		Log.d(TAG_MAPA, "GPS Habilitado.")
		Toast.makeText(this, "GPS Habilitado.", Toast.LENGTH_LONG).show();
	}

	override fun onProviderDisabled(provider: String?) {
		Log.d(TAG_MAPA, "GPS Desabilitado.")
		Toast.makeText(this, "GPS Desabilitado.", Toast.LENGTH_LONG).show();
	}
}
