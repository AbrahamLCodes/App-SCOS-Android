package scos.app.bitacora.forms

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import scos.app.bitacora.R
import scos.app.bitacora.adapters.FallaAdapter
import scos.app.bitacora.adapters.ImagenAdapter
import scos.app.bitacora.modelos.Falla
import java.lang.Exception


class ReporteActivity :
    AppCompatActivity(),
    View.OnClickListener {


    private var imagenUpdloaded = false
    private var insert: Boolean? = null
    private var position: Int? = null

    private lateinit var inputProblema: TextView
    private lateinit var inputDesc: TextView
    private lateinit var imagen: ImageView

    companion object {
        lateinit var fallaAdapter: FallaAdapter
        lateinit var imagenAdapter: ImagenAdapter
        lateinit var fallasList: MutableList<Falla>
        lateinit var imagesEncodedList: ArrayList<String>
        lateinit var dialog: Dialog
        lateinit var recyclerMain: RecyclerView
        lateinit var recyclerDialog: RecyclerView
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
            }
        }
    }

    private fun buildDialog() {

        imagenAdapter = ImagenAdapter()

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_detallle)

        val yesBtn = dialog.findViewById(R.id.yesBtn) as Button
        val noBtn = dialog.findViewById(R.id.noBtn) as Button
        val selecImg = dialog.findViewById(R.id.selectImg) as TextView

        recyclerDialog = dialog.findViewById(R.id.recycler)

        inputProblema = dialog.findViewById(R.id.inputproblema) as EditText
        inputDesc = dialog.findViewById(R.id.textArea_information) as EditText

        yesBtn.setOnClickListener {
            if (checkFields()) {
                if (insert!!) {
                    fallasList.add(
                        Falla(
                            inputProblema.text.toString(),
                            inputDesc.text.toString(),
                            imagesEncodedList
                        )
                    )
                } else {
                    fallasList[position!!] = Falla(
                        inputProblema.text.toString(),
                        inputDesc.text.toString(),
                        imagesEncodedList
                    )
                }
                dialog.dismiss()
                setRecyclerData()
            }
        }
        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        selecImg.setOnClickListener {
            selectImage()
        }
        dialog.show()
    }

    private fun setDialogRecyclerData() {
        imagenAdapter = ImagenAdapter()
        imagenAdapter.submitList(imagesEncodedList)
        recyclerDialog.apply {
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            adapter = imagenAdapter
        }
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
        imagesEncodedList = ArrayList()
    }

    private fun selectImage() {
        val intent = Intent()
        imagenUpdloaded = true
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona fotos"), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 100) {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        if (data.clipData != null) {
                            val count =
                                data.clipData!!.itemCount //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.

                            var i = 0
                            while (i < count) {
                                val imageUri = data.clipData!!.getItemAt(i).uri
                                Log.d("COUNTER", "Posición: $i")
                                imagesEncodedList.add(imageUri.toString())
                                i++
                            }
                        }
                    }
                } else if (data!!.data != null) {
                    val imagePath = data.data!!.path
                    imagesEncodedList.add(imagePath!!)
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
                setDialogRecyclerData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
        imagen.setImageURI(Uri.parse(falla.getUris()[0]))
    }

    private fun initComponents() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val btnProblema = findViewById<Button>(R.id.btnProblema)
        val selectImg = findViewById<TextView>(R.id.selectImg)
        val takeImg = findViewById<TextView>(R.id.takeImg)
        imagesEncodedList = ArrayList()
        //val Asunto = findViewById<EditText>(R.id.inputAsunto)
        //val tecnico = findViewById<EditText>(R.id.inputTecnico)
        recyclerMain = findViewById(R.id.recycler)
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

        if (!insert!!) {
            position = intent.getIntExtra("position", 0)
            setComponents(position!!)
        }
        setRecyclerData()
    }
}