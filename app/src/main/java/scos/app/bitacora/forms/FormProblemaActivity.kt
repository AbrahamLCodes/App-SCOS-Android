package scos.app.bitacora.forms

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import scos.app.bitacora.R


class FormProblemaActivity :
    AppCompatActivity(),
    View.OnClickListener {

    private lateinit var inputProblema: TextView
    private lateinit var inputTecnico: TextView
    private lateinit var inputDesc: TextView
    private lateinit var imagen: ImageView
    private var imagenUpdloaded = false
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_problema)
        initComponents()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnSolucion -> {
                    if (checkFields()) {
                        startActivity(Intent(this, FormSolucionActivity::class.java).apply {
                            putExtra("falla",inputProblema.text.toString())
                            putExtra("tecnico",inputTecnico.text.toString())
                            putExtra("descProblema", inputProblema.text.toString())
                            putExtra("uriStringProblema", imageUri.toString())
                        })
                        finish()
                    }
                }
                R.id.selectImg -> {
                    selectImage()
                }
                R.id.takeImg -> {
                    selectImage()
                }
            }
        }
    }

    private fun selectImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            imagen.setImageURI(imageUri)
            imagenUpdloaded = true
        }
    }

    private fun checkFields(): Boolean {
        return if (inputProblema.text.isEmpty()) {
            makeToast("Introduce el nombre problema")
            inputProblema.requestFocus()
            false
        } else if (inputTecnico.text.isEmpty()) {
            makeToast("Introduce el nombre del técnico")
            inputTecnico.requestFocus()
            false
        } else if (inputDesc.text.isEmpty()) {
            makeToast("Introduce la descripción del problema")
            inputDesc.requestFocus()
            false
        } else if (!imagenUpdloaded) {
            makeToast("Selecciona o toma una foto de tu galería")
            imagen.requestFocus()
            false
        } else {
            true
        }
    }

    private fun makeToast(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun initComponents() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val btnSolucion = findViewById<Button>(R.id.btnSolucion)
        val selectImg = findViewById<TextView>(R.id.selectImg)
        val takeImg = findViewById<TextView>(R.id.takeImg)

        inputProblema = findViewById(R.id.inputProblema)
        inputTecnico = findViewById(R.id.inputTecnico)
        inputDesc = findViewById(R.id.inputDesc)
        imagen = findViewById(R.id.imagen)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        btnSolucion.setOnClickListener(this)
        selectImg.setOnClickListener(this)
        takeImg.setOnClickListener(this)
    }
}