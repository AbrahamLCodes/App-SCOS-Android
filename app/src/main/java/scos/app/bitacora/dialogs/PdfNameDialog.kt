package scos.app.bitacora.dialogs

import android.content.pm.PackageManager
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import scos.app.bitacora.R
import scos.app.bitacora.pdfservices.PdfMaker
import java.util.*

class PdfNameDialog(
    private var folio: String,
    private var asunto: String,
    private var admin: String,
    private var tecnico: String,
    private var cel: String,
    private var fracc: String,
    private var isFalla: Boolean
) : DialogFragment(), View.OnClickListener {
    private lateinit var inputNamePdf: EditText

    private val date = Date()
    private val meses = listOf(
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    )

    private val monthNumber = DateFormat.format("M", date) // 06
    private val day = DateFormat.format("dd", date) // 20
    private val year = DateFormat.format("yyyy", date) // 2013
    private val fecha = "$day de ${meses[monthNumber.toString().toInt() - 1]} del $year"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_file_name, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
    }

    private fun initComponents(v: View?) {
        val btnAnadir = v?.findViewById(R.id.btnConvertir) as Button
        val btnCancelar = v.findViewById(R.id.btnCancelar) as Button
        inputNamePdf = v.findViewById(R.id.inputNamePdf) as EditText
        btnAnadir.setOnClickListener(this)
        btnCancelar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnConvertir -> {
                    makeToast("Creando PDF")
                    Thread {
                        PdfMaker(
                            context!!,
                            activity!!.contentResolver,
                            folio,
                            asunto,
                            admin,
                            tecnico,
                            cel,
                            fracc,
                            isFalla,
                            inputNamePdf.text.toString(),
                            fecha
                        ).makePDF()
                    }.start()
                    dismiss()
                }
                R.id.btnCancelar -> {
                    dismiss()
                }
            }
        }
    }

    private fun makeToast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }
}