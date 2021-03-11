package scos.app.bitacora.mainactivities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import scos.app.bitacora.R
import scos.app.bitacora.adapters.RegistroAdapter
import scos.app.bitacora.dialogs.PdfNameDialog
import scos.app.bitacora.dialogs.RegistroDialog
import scos.app.bitacora.modelos.Registro
import kotlin.properties.Delegates

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
    private lateinit var dialog: RegistroDialog
    private lateinit var dialog2: PdfNameDialog
    private lateinit var btnProblema: Button
    private lateinit var txtProblema: TextView
    private lateinit var toolbar: Toolbar

    companion object {
        lateinit var registroAdapter: RegistroAdapter
        lateinit var fallasList: MutableList<Registro>
        lateinit var recyclerMain: RecyclerView
        lateinit var pdfName: String
        var isfallaIntent by Delegates.notNull<Boolean>()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporte)
        initComponents()
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Salir")
            .setMessage("¿Seguro que quiere Salir del registro?")
            .setPositiveButton("si") { _, _ -> finish() }
            .setNegativeButton("no") { _, _ -> closeOptionsMenu() }
            .show()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnProblema -> {
                    buildDialog()
                }
                R.id.takeImg -> {
                    finish()
                }
                R.id.makePdf -> {
                    if (checkFields()) {
                        buildDialogName()
                    }
                }
            }
        }
    }

    private fun checkFields(): Boolean {
        when {
            folio.text.toString() == "" -> {
                makeToast("Introduce un folio")
                folio.requestFocus()
                return false
            }
            asunto.text.toString() == "" -> {
                makeToast("Introduce el asunto")
                asunto.requestFocus()
                return false
            }
            admin.text.toString() == "" -> {
                makeToast("Introduce el administrador cliente")
                admin.requestFocus()
                return false
            }
            tecnico.text.toString() == "" -> {
                makeToast("Introduce el responsable")
                tecnico.requestFocus()
                return false
            }
            cel.text.toString() == "" -> {
                makeToast("Introduce el Cel. del responsable")
                cel.requestFocus()
                return false
            }
            fracc.text.toString() == "" -> {
                makeToast("Introduce el fraccionamiento")
                fracc.requestFocus()
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun buildDialog() {
        dialog = RegistroDialog(null, null, isfallaIntent)
        dialog.show(supportFragmentManager, "custom dialog")
    }

    private fun buildDialogName() {
        dialog2 = PdfNameDialog(
            folio.text.toString(),
            asunto.text.toString(),
            admin.text.toString(),
            tecnico.text.toString(),
            cel.text.toString(),
            fracc.text.toString(),
            isfallaIntent
        )
        dialog2.show(supportFragmentManager, "custom dialog 2")

    }

    private fun setRecyclerData() {
        registroAdapter = RegistroAdapter()
        registroAdapter.submitList(fallasList)
        recyclerMain.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = registroAdapter
        }

        // Reset collections and validations to perform a new record
        imagenUpdloaded = false
    }

    private fun makeToast(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun checkIsFalla() {
        if (!isfallaIntent) {
            //configurar solucion
            btnProblema.text = "Añadir Solucion"
            txtProblema.text = "Soluciones:"
            toolbar.title = "Registro de Solucion"
        }
    }

    private fun initComponents() {
        //Fields
        txtProblema = findViewById(R.id.txtProblema)
        folio = findViewById(R.id.folio)
        asunto = findViewById(R.id.asunto)
        admin = findViewById(R.id.admin)
        tecnico = findViewById(R.id.tecnico)
        cel = findViewById(R.id.celTecnico)
        fracc = findViewById(R.id.fracc)
        isfallaIntent = intent.getBooleanExtra("isfalla", true)
        //Buttons
        toolbar = findViewById(R.id.toolbar)
        btnProblema = findViewById(R.id.btnProblema)
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
        checkIsFalla()
    }
}