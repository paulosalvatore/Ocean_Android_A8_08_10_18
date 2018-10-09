package br.com.paulosalvatore.ocean_android_a8_08_10_18

data class Posicao(var id: Int,
                   var latitude: Double,
                   var longitude: Double,
                   var dataHora: String) {

	constructor(latitude: Double, longitude: Double, dataHora: String) :
			this(0, latitude, longitude, dataHora) {
		this.latitude = latitude
		this.longitude = longitude
		this.dataHora = dataHora
	}
}
