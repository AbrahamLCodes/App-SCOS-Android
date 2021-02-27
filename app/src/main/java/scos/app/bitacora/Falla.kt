package scos.app.bitacora

import android.net.Uri
import java.io.Serializable

class Falla(
    @JvmField private var falla: String,
    @JvmField private var tecnico: String,
    @JvmField private var fallaDesc: String,
    @JvmField private var fallaImg: String,
    @JvmField private var solucionDesc: String,
    @JvmField private var solucionImg: String,
    @JvmField private var dia: String,
    @JvmField private var mes: String,
    @JvmField private var anio: String,
    @JvmField private var hora: String,
    @JvmField private var min: String
): Serializable {
    fun getFalla() = falla
    fun getTecnico() = tecnico
    fun getFallaDesc() = fallaDesc
    fun getFallaImg() = fallaImg
    fun getSolucionDesc() = solucionDesc
    fun getSolucionImg() = solucionImg
    fun getDia() = dia
    fun getMes() = mes
    fun getAnio() = anio
    fun getHora() = hora
    fun getMin() = min
}