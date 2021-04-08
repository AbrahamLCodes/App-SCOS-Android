package scos.app.bitacora.dialogs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import scos.app.bitacora.R
import scos.app.bitacora.adapters.RegistroAdapter
import scos.app.bitacora.adapters.ImagenAdapter
import scos.app.bitacora.mainactivities.ReporteActivity
import scos.app.bitacora.modelos.Registro

class RegistroDialog(position: Int?, registro: Registro?) : DialogFragment(),
    View.OnClickListener {

    private lateinit var inputProblema: EditText
    private lateinit var selecImageText: TextView
    private lateinit var inputDesc: EditText
    private lateinit var recyclerDialog: RecyclerView
    private lateinit var imagesEncodedList: ArrayList<String>
    private lateinit var imagenAdapter: ImagenAdapter
    private var position = position
    private var falla = registro

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_registro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents(view)
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
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
        ReporteActivity.fallasList.add(
            Registro(
                inputProblema.text.toString(),
                inputDesc.text.toString(),
                imagesEncodedList
            )
        )
        updateReporteUI()
    }

    private fun update() {
        ReporteActivity.fallasList[position!!] = Registro(
            inputProblema.text.toString(),
            inputDesc.text.toString(),
            imagesEncodedList
        )
        updateReporteUI()
    }

    private fun updateReporteUI() {
        ReporteActivity.registroAdapter = RegistroAdapter()
        ReporteActivity.registroAdapter.submitList(ReporteActivity.fallasList)
        ReporteActivity.recyclerMain.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = ReporteActivity.registroAdapter
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
                makeToast("Introduce el problema")
                inputProblema.requestFocus()
                false
            }
            inputDesc.text.isEmpty() -> {
                makeToast("Introduce la descripción del problema")
                inputDesc.requestFocus()
                false
            }
            imagesEncodedList.isEmpty() -> {
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
                           var i = 0
                            while (i < 2) {
                                val imageUri = data.clipData!!.getItemAt(i).uri
                                imagesEncodedList.add(imageUri.toString())
                                i++
                            }
                        } else {
                            val imagePath = data.data
                            imagesEncodedList.add(imagePath.toString())
                        }
                    }
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
        inputProblema.setText(falla!!.getFalla())
        inputDesc.setText(falla!!.getFallaDesc())

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