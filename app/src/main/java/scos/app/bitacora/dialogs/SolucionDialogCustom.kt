package scos.app.bitacora.dialogs

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import scos.app.bitacora.R
import scos.app.bitacora.adapters.FallaAdapter
import scos.app.bitacora.adapters.ImagenAdapter
import scos.app.bitacora.forms.FormSolucionActivity
import scos.app.bitacora.forms.ReporteActivity
import scos.app.bitacora.modelos.Falla
import scos.app.bitacora.modelos.Solucion
import java.lang.Exception

class SolucionDialogCustom(position: Int?, solucion: Solucion?): DialogFragment(), View.OnClickListener {

    private lateinit var inputProblema: EditText
    private lateinit var selecImageText: TextView
    private lateinit var inputDesc: EditText

    private lateinit var recyclerDialog: RecyclerView
    private lateinit var imagesEncodedList: ArrayList<String>
    private lateinit var imagenAdapter: ImagenAdapter

    private var position = position
    private var falla = solucion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_detalle_solucion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents(view)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher
            .addCallback(this,object :OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    Log.d("lol", "Fragment back pressed invoked")
                    // Do custom work here

                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }

            })
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.noBtn ->
                    dismiss()
                R.id.yesBtn -> {
                    if (checkfields()) {
                        if (position != null && falla != null) {
                            Toast.makeText(context, "Actualizando...", Toast.LENGTH_SHORT).show()
                            update()
                        } else {
                            insert()
                        }
                        dismiss()
                    }
                }
                R.id.selectImg -> {
                    selectImage()
                }
            }
        }
    }

    private fun insert() {
        FormSolucionActivity.fallasList.add(
            Falla(
                inputProblema.text.toString(),
                inputDesc.text.toString(),
                imagesEncodedList
            )
        )
        updateReporteUI()
    }

    private fun update() {
        FormSolucionActivity.fallasList[position!!] = Falla(
            inputProblema.text.toString(),
            inputDesc.text.toString(),
            imagesEncodedList
        )
        updateReporteUI()
    }

    private fun updateReporteUI() {
        FormSolucionActivity.fallaAdapter = FallaAdapter()
        FormSolucionActivity.fallaAdapter.submitList(FormSolucionActivity.fallasList)
        FormSolucionActivity.recyclerMain.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = FormSolucionActivity.fallaAdapter
        }
    }

    private fun selectImage() {

        imagesEncodedList = ArrayList()

        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona fotos"), 100)
    }

    private fun checkfields(): Boolean {
        return when {
            inputProblema.text.isEmpty() -> {
                makeToast("Introduce la solucion")
                inputProblema.requestFocus()
                false
            }
            inputDesc.text.isEmpty() -> {
                makeToast("Introduce la descripción de la solucion")
                inputDesc.requestFocus()
                false
            }
            imagesEncodedList.size == 0 -> {
                makeToast("Selecciona al menos una imagen")
                false
            }
            else -> {
                true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
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
                setRecyclerData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setRecyclerData() {
        imagenAdapter = ImagenAdapter()
        imagenAdapter.submitList(imagesEncodedList)
        recyclerDialog.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = imagenAdapter
        }
    }

    private fun makeToast(mensaje: String) {
        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun setUpComponents() {
        inputProblema.setText(falla!!.getSolucion())
        inputDesc.setText(falla!!.getSolucionDescripcion())

        var i = 0
        while (i < falla!!.getUris().size) {
            imagesEncodedList.add(falla!!.getUris()[i])
            i++
        }
        setRecyclerData()
    }

    private fun initComponents(v: View) {
        val btnCanel = v.findViewById<Button>(R.id.noBtn)
        val btnConfrim = v.findViewById(R.id.yesBtn) as Button
        selecImageText = v.findViewById(R.id.selectImg) as TextView
        inputProblema = v.findViewById(R.id.inputproblema) as EditText
        inputDesc = v.findViewById(R.id.textArea_information) as EditText

        imagesEncodedList = ArrayList()
        btnCanel.setOnClickListener(this)
        btnConfrim.setOnClickListener(this)
        selecImageText.setOnClickListener(this)
        recyclerDialog = view?.findViewById(R.id.recyclerDialog) as RecyclerView

        if (falla != null) {
            setUpComponents()
        }
    }
}