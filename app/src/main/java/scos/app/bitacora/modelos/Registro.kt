package scos.app.bitacora.modelos

import java.io.Serializable

class Registro(
    @JvmField private var falla: String,
    @JvmField private var fallaDesc: String,
    @JvmField private var uris: MutableList<String>
): Serializable {
    fun getFalla() = falla
    fun getFallaDesc() = fallaDesc
    fun getUris() = uris
}