package scos.app.bitacora.modelos

import java.io.Serializable

class Registro(
    @JvmField var falla: String,
    @JvmField var fallaDesc: String,
    @JvmField var uris: MutableList<String>
): Serializable {
    fun getFalla() = falla
    fun getFallaDesc() = fallaDesc
    fun getUris() = uris
}