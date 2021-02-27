package scos.app.bitacora

import java.io.Serializable

class Falla(
    @JvmField private var falla: String,
    @JvmField private var fallaDesc: String,
    @JvmField private var fallaUri: String
): Serializable {
    fun getFalla() = falla
    fun getFallaDesc() = fallaDesc
    fun getFallaUri() = fallaUri
}