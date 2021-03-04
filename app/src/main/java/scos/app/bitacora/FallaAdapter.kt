package scos.app.bitacora

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import scos.app.bitacora.forms.FormFallaActivity
import scos.app.bitacora.forms.ViewFallaActivity

class FallaAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var items: MutableList<Falla>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FallaViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_detalle,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FallaViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(fallas: List<Falla>) {
        items = ArrayList(fallas)
    }

    inner class FallaViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fallaTxt: TextView = itemView.findViewById(R.id.falla)
        private val itemImagen: ImageView = itemView.findViewById(R.id.imagenItem)
        private val descrTxt: TextView = itemView.findViewById(R.id.txtdescription)

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, FormFallaActivity::class.java)
                intent.apply {
                    putExtra("position", adapterPosition)
                }
                itemView.context.startActivity(intent)
            }
            itemView.setOnLongClickListener {
                MaterialAlertDialogBuilder(itemView.context).setTitle(
                    "Falla: " + fallaTxt.text.toString().toUpperCase()
                ).setMessage("¿Qué desea hacer?").setNeutralButton("Ver") { _, _ ->
                    //En construcción
                }.setPositiveButton("Editar") { _, _ ->
                    //En construcción
                }.setNegativeButton("Eliminar") { _, _ ->
                    FormFallaActivity.fallasList.removeAt(adapterPosition)
                    FormFallaActivity.fallaAdapter = FallaAdapter()
                    FormFallaActivity.fallaAdapter.submitList(FormFallaActivity.fallasList)
                    FormFallaActivity.recycler.apply {
                        layoutManager = GridLayoutManager(context, 1)
                        adapter = FormFallaActivity.fallaAdapter
                    }
                }.show()
                return@setOnLongClickListener true
                 }
        }

        fun bind(falla: Falla) {
            fallaTxt.text = falla.getFalla()
            descrTxt.text = falla.getFallaDesc()
            itemImagen.setImageURI(Uri.parse(items[adapterPosition].getFallaUri()))
            Log.d("URI", falla.getFallaUri())
        }
    }
}

