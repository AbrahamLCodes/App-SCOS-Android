package scos.app.bitacora.modelos

import java.io.Serializable

class Solucion(
    @JvmField private var solucion: String,
    @JvmField private var solucionDescr: String,
    @JvmField private var uris: MutableList<String>): Serializable {
    fun getSolucion() = solucion
    fun getSolucionDescripcion() = solucionDescr
    fun getUris() = uris
}