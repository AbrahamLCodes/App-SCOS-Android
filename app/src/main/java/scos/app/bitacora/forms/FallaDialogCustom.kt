package scos.app.bitacora.forms

import android.app.Activity
import android.app.Dialog
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
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import scos.app.bitacora.R
import scos.app.bitacora.adapters.ImagenAdapter
import scos.app.bitacora.modelos.Falla
import java.lang.Exception

class FallaDialogCustom: DialogFragment(), View.OnClickListener {
    private lateinit var inputProblema: EditText
    private lateinit var selecImageText: TextView
    private var insert: Boolean? = true
    private lateinit var inputDesc: EditText
    private var imagenUpdloaded = false
    private var position: Int? = null
    companion object{
        lateinit var recyclerDialog: RecyclerView
        lateinit var imagesEncodedList: ArrayList<String>
        lateinit var imagenAdapter: ImagenAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_detallle,container,false)
        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        val btnCanel = view?.findViewById(R.id.noBtn) as Button
        val btnConfrim = view?.findViewById(R.id.yesBtn) as Button
        selecImageText = view?.findViewById(R.id.selectImg) as TextView
        inputProblema= view?.findViewById(R.id.inputproblema) as EditText
        inputDesc = view?.findViewById(R.id.textArea_information) as EditText
        imagesEncodedList = ArrayList()
        // val selectImage = view?.findViewById(R.id.selectImg) as TextView
         btnCanel.setOnClickListener(this)
        btnConfrim.setOnClickListener(this)
        selecImageText.setOnClickListener(this)
        recyclerDialog = view?.findViewById(R.id.recyclerDialog) as RecyclerView


    }
    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.noBtn ->
                    dismiss()
                R.id.yesBtn ->{
                    if (checkfields()){
                        if (insert!!){
                            ReporteActivity.fallasList.add(
                                Falla(
                                    inputProblema.text.toString(),
                                    inputDesc.text.toString(),
                                    imagesEncodedList
                                )
                            )

                        }else {
                            ReporteActivity.fallasList[position!!] = Falla(
                                inputProblema.text.toString(),
                                inputDesc.text.toString(),
                                imagesEncodedList
                            )
                    }
                    }
                    dismiss()
                }
                R.id.selectImg->{
                    selectImage()
                }
            }
        }

    }


    private fun selectImage() {
        val intent = Intent()
        imagenUpdloaded = true
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona fotos"), 100)

    }
    private fun checkfields(): Boolean {
        return if (inputProblema.text.isEmpty()) {
            Toast.makeText(context,"lol",Toast.LENGTH_SHORT)
            inputProblema.requestFocus()
            false
        } else if (inputDesc.text.isEmpty()) {
            Toast.makeText(context,"Introduce la descripción del problema",Toast.LENGTH_SHORT)
            inputDesc.requestFocus()
            false
        } else if (!imagenUpdloaded) {
            Log.d("TOAS IMAGE","AAAAAA")
            Toast.makeText(context,"Selecciona o toma una foto de tu galería",Toast.LENGTH_SHORT)
            false
        } else {
            true
        }
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







}