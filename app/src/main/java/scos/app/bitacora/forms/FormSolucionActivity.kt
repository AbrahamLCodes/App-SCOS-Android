package scos.app.bitacora.forms

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import scos.app.bitacora.Falla
import scos.app.bitacora.MainActivity
import scos.app.bitacora.R
import java.util.*

class FormSolucionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var desc: EditText
    private lateinit var imagen: ImageView
    private var imagenUploaded = false
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_solucion)
        initComponents()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, FormProblemaActivity::class.java))
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnSolucion -> {
                    if (checkFields()) {
                        MainActivity.fallasList.add(
                            Falla(
                                intent.getStringExtra("falla")!!,
                                intent.getStringExtra("tecnico")!!,
                                intent.getStringExtra("descProblema")!!,
                                intent.getStringExtra("uriStringProblema")!!,
                                desc.text.toString(),
                                imageUri.toString(),
                                Calendar.getInstance().get(Calendar.YEAR).toString(),
                                Calendar.getInstance().get(Calendar.MONTH).toString(),
                                Calendar.getInstance().get(Calendar.YEAR).toString(),
                                Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString(),
                                Calendar.getInstance().get(Calendar.MINUTE).toString()
                            )
                        )
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
            imagenUploaded = true
        }
    }

    private fun checkFields(): Boolean {
        return if (desc.text.isEmpty()) {
            makeShortToast("Introduce la descripción de la solución")
            desc.requestFocus()
            false
        } else if (!imagenUploaded) {
            makeShortToast("Selecciona o toma una foto para la solución")
            imagen.requestFocus()
            false
        } else {
            true
        }
    }

    private fun makeShortToast(mensaje: String) {
        Toast.makeText(applicationContext, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun initComponents() {
        val selectImg = findViewById<TextView>(R.id.selectImg)
        val takeImg = findViewById<TextView>(R.id.takeImg)
        val btnSolucion = findViewById<Button>(R.id.btnSolucion)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        desc = findViewById(R.id.inputDesc)
        imagen = findViewById(R.id.imagen)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        selectImg.setOnClickListener(this)
        takeImg.setOnClickListener(this)
        btnSolucion.setOnClickListener(this)
    }
}