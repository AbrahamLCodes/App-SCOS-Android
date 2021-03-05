package scos.app.bitacora.forms

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import scos.app.bitacora.R
import scos.app.bitacora.adapters.FallaAdapter
import scos.app.bitacora.dialogs.SolucionDialogCustom
import scos.app.bitacora.modelos.Falla

class FormSolucionActivity: AppCompatActivity(), View.OnClickListener{
    private var imagenUpdloaded = false
    private var insert: Boolean? = null
    private var position: Int? = null

    private lateinit var inputProblema: TextView
    private lateinit var inputDesc: TextView
    private lateinit var imagen: ImageView
    private lateinit var dialog: SolucionDialogCustom


    companion object {
        lateinit var fallaAdapter: FallaAdapter
        lateinit var fallasList: MutableList<Falla>
        lateinit var recyclerMain: RecyclerView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_solucion)
        initComponents()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnProblema -> {
                    buildDialog()
                }
            }
        }
    }
    private fun buildDialog(){
        dialog = SolucionDialogCustom(null, null)
        dialog.show(supportFragmentManager, "custom dialog")
    }
    private fun setRecyclerData(){
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

    private fun setComponents(position: Int) {
        val falla = fallasList[position]
        inputProblema.text = falla.getFalla()
        inputDesc.text = falla.getFallaDesc()
        imagen.setImageURI(Uri.parse(falla.getUris()[0]))
    }

    private fun initComponents() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val btnProblema = findViewById<Button>(R.id.btnProblema)
        val selectImg = findViewById<TextView>(R.id.selectImg)
        val takeImg = findViewById<TextView>(R.id.takeImg)

       recyclerMain = findViewById(R.id.recycler)
        fallasList = ArrayList()

        insert = intent.getBooleanExtra("insert", false)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        btnProblema.setOnClickListener(this)
        selectImg.setOnClickListener(this)
        takeImg.setOnClickListener(this)

        if (!insert!!) {
            position = intent.getIntExtra("position", 0)
            setComponents(position!!)
        }
        setRecyclerData()
    }
}