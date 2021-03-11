package scos.app.bitacora.dialogs

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import scos.app.bitacora.R
import scos.app.bitacora.forms.ReporteActivity
import scos.app.bitacora.pdfservices.PdfMaker
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class Dialog_PDF_Name(folio: String,asunto: String, admin: String, tecnico: String, cel: String, fracc: String) : DialogFragment(), View.OnClickListener{
    private lateinit var inputNamePdf: EditText
    private var folio = folio
    private var admin = admin
    private var asunto = asunto
    private var tecnico = tecnico
    private var cel = cel
    private var fracc = fracc
    val date = Date()
    val meses = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    val dayOfTheWeek = DateFormat.format("EEEE", date) // Thursday
    val monthNumber = DateFormat.format("M", date) // 06
    val day = DateFormat.format("dd", date) // 20
    val monthString = DateFormat.format("MMMM", date) // Jun
    val year = DateFormat.format("yyyy", date) // 2013
    val fecha = "$day de ${meses[monthNumber.toString().toInt()]} del $year"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_file_name,container,false)
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
        if(v != null){
            when(v.id){
                R.id.btnConvertir->{
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
                            true,
                            inputNamePdf.text.toString(),
                            fecha
                        ).makePDF()

                    }.start()

                    dismiss()
                }
                R.id.btnCancelar->{
                    dismiss()
                }
            }
        }
    }

    private fun makeToast(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show()

    }
}