package scos.app.bitacora.forms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import scos.app.bitacora.mainactivities.MainActivity
import scos.app.bitacora.R

class FormFallaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var nombre: TextView
    private lateinit var desc: TextView

    //private lateinit var imagen: ImageView
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_falla)
        initComponents()
    }

    override fun onResume() {
        super.onResume()
        setComponents()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.editar -> {
                    startActivity(Intent(this, ReporteActivity::class.java).apply {
                        putExtra("position", position)
                        putExtra("insert", false)
                    })
                }
            }
        }
    }

    private fun setComponents() {
        val falla = MainActivity.fallasList[position!!]

        nombre.text = falla.getFalla()
        desc.text = falla.getFallaDesc()
        // imagen.setImageURI(Uri.parse(falla.getFallaUri()))
    }

    private fun initComponents() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        nombre = findViewById(R.id.nombre)
        desc = findViewById(R.id.desc)
        // imagen = findViewById(R.id.imagen)
        val editar = findViewById<ImageView>(R.id.editar)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        editar.setOnClickListener(this)

        position = intent.getIntExtra("position", 0)
        setComponents()
    }
}