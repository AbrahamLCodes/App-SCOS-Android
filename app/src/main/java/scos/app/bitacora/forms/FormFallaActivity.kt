package scos.app.bitacora.forms

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import scos.app.bitacora.Falla
import scos.app.bitacora.FallaAdapter
import scos.app.bitacora.MainActivity
import scos.app.bitacora.MainActivity.Companion.fallasList
import scos.app.bitacora.MainActivity.Companion.recycler
import scos.app.bitacora.R

class FormFallaActivity :
    AppCompatActivity(),
    View.OnClickListener {

    private lateinit var inputProblema: TextView
    private lateinit var inputDesc: TextView
    private lateinit var imagen: ImageView
    private var imagenUpdloaded = false
    private var imageUri: Uri? = null
    private var insert: Boolean? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_falla)
        initComponents()
    }
    companion object {
        lateinit var recycler: RecyclerView
        lateinit var fallaAdapter: FallaAdapter
        lateinit var fallasList: MutableList<Falla>
        lateinit var dialog: Dialog

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnProblema -> {
                    dialog = Dialog(this)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_detallle)
                    val yesBtn = dialog.findViewById(R.id.yesBtn) as Button
                    val noBtn = dialog.findViewById(R.id.noBtn) as Button
                    val selecImg = dialog.findViewById(R.id.selectImg) as TextView
                    imagen = dialog.findViewById(R.id.imagen) as ImageView
                    inputProblema = dialog.findViewById(R.id.inputproblema) as EditText
                    inputDesc = dialog.findViewById(R.id.textArea_information) as EditText
                    yesBtn.setOnClickListener {
                        if (checkFields()) {
                            Log.d("lol","dsfafdasfasfafdasf"+insert)
                            if (insert!!) {
                                fallasList.add(
                                    Falla(
                                        inputProblema.text.toString(),
                                        inputDesc.text.toString(),
                                        imageUri.toString()
                                    )
                                )
                                dialog.dismiss()
                                setRecyclerData()
                            } else {
                                fallasList[position!!] = Falla(
                                    inputProblema.text.toString(),
                                    inputDesc.text.toString(),
                                    imageUri.toString()
                                )
                            }
                        }

                    }
                    noBtn.setOnClickListener{
                        dialog.dismiss()
                    }
                    selecImg.setOnClickListener {
                        selectImage()
                    }
                    dialog.show()
                }
            }
        }
    }
    fun setRecyclerData() {
        fallaAdapter = FallaAdapter()
        fallaAdapter.submitList(fallasList)
        recycler.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = fallaAdapter
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

    private fun setComponents(position: Int) {
        val falla = fallasList[position]
    inputProblema.text = falla.getFalla()
    inputDesc.text = falla.getFallaDesc()
    imagen.setImageURI(Uri.parse(falla.getFallaUri()))
    }

    private fun initComponents() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val btnProblema = findViewById<Button>(R.id.btnProblema)
        val selectImg = findViewById<TextView>(R.id.selectImg)
        val takeImg = findViewById<TextView>(R.id.takeImg)
        val Asunto = findViewById<EditText>(R.id.inputAsunto)
        val tecnico = findViewById<EditText>(R.id.inputTecnico)
        recycler = findViewById(R.id.recycler)
        fallasList = ArrayList()

        //inputProblema = findViewById(R.id.inputProblema)
        //inputDesc = findViewById(R.id.inputDesc)

        insert = intent.getBooleanExtra("insert", false)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        btnProblema.setOnClickListener(this)
        selectImg.setOnClickListener(this)
        takeImg.setOnClickListener(this)
        Log.d("lol","dsfafdasfasfafdasf"+insert)

        if (!insert!!) {
            position = intent.getIntExtra("position", 0)
            setComponents(position!!)
        }
        setRecyclerData()
    }
}