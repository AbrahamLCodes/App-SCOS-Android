package scos.app.bitacora.forms

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import scos.app.bitacora.R
import scos.app.bitacora.adapters.FallaAdapter
import scos.app.bitacora.dialogs.FallaDialogCustom
import scos.app.bitacora.modelos.Falla
import scos.app.bitacora.pdfservices.PdfMaker

class ReporteActivity :
    AppCompatActivity(),
    View.OnClickListener {
    private var imagenUpdloaded = false

    //Fields
    private lateinit var folio: EditText
    private lateinit var asunto: EditText
    private lateinit var admin: EditText
    private lateinit var tecnico: EditText
    private lateinit var cel: EditText
    private lateinit var fracc: EditText
    private lateinit var dialog: FallaDialogCustom


    companion object {
        lateinit var fallaAdapter: FallaAdapter
        lateinit var fallasList: MutableList<Falla>
        lateinit var recyclerMain: RecyclerView

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte)
        initComponents()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnProblema -> {
                    buildDialog()
                }
                R.id.makePdf -> {
                    makeToast("Creando PDF")
                    if (checkFields()) {
                        Thread {
                            PdfMaker(
                                applicationContext,
                                contentResolver,
                                folio.text.toString(),
                                asunto.text.toString(),
                                admin.text.toString(),
                                tecnico.text.toString(),
                                cel.text.toString(),
                                fracc.text.toString()
                            ).makePDF()
                            this.runOnUiThread {
                                makeToast("PDF Creado")
                            }
                        }.start()
                    }
                }
            }
        }
    }

    private fun checkFields(): Boolean {
        if (folio.text.toString() == "") {
            makeToast("Introduce un folio")
            folio.requestFocus()
            return false
        } else if (asunto.text.toString() == "") {
            makeToast("Introduce el asunto")
            asunto.requestFocus()
            return false
        } else if (admin.text.toString() == "") {
            makeToast("Introduce el administrador cliente")
            admin.requestFocus()
            return false
        } else if (tecnico.text.toString() == "") {
            makeToast("Introduce el responsable")
            tecnico.requestFocus()
            return false
        } else if (cel.text.toString() == "") {
            makeToast("Introduce el Cel. del responsable")
            cel.requestFocus()
            return false
        } else if (fracc.text.toString() == "") {
            makeToast("Introduce el fraccionamiento")
            fracc.requestFocus()
            return false
        } else {
            return true
        }
    }

    private fun buildDialog() {
        dialog = FallaDialogCustom(null, null)
        dialog.show(supportFragmentManager, "custom dialog")
    }

    private fun setRecyclerData() {
        fallaAdapter = FallaAdapter()
        fallaAdapter.submitList(fallasList)
        recyclerMain.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = fallaAdapter
        }

        // Reset collections and validations to perform a new record
        imagenUpdloaded = false
    }

    private fun makeToast(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun initComponents() {

        //Fields
        folio = findViewById(R.id.folio)
        asunto = findViewById(R.id.asunto)
        admin = findViewById(R.id.admin)
        tecnico = findViewById(R.id.tecnico)
        cel = findViewById(R.id.celTecnico)
        fracc = findViewById(R.id.fracc)

        //Buttons
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val btnProblema = findViewById<Button>(R.id.btnProblema)
        val makePdf = findViewById<TextView>(R.id.makePdf)
        val takeImg = findViewById<TextView>(R.id.takeImg)

        //Collections
        recyclerMain = findViewById(R.id.recycler)
        fallasList = ArrayList()

        //Setting up Click Events
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        btnProblema.setOnClickListener(this)
        makePdf.setOnClickListener(this)
        takeImg.setOnClickListener(this)

        //Initializating collections
        setRecyclerData()
    }
}