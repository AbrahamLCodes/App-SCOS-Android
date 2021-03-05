package scos.app.bitacora.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import scos.app.bitacora.modelos.Falla
import scos.app.bitacora.R
import scos.app.bitacora.forms.ReporteActivity

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
                editar()
            }
            itemView.setOnLongClickListener {
                MaterialAlertDialogBuilder(itemView.context).setTitle(
                    "Falla: " + fallaTxt.text.toString().toUpperCase()
                ).setMessage("¿Qué desea hacer?")
                    .setPositiveButton("Editar") { _, _ ->
                        editar()
                    }.setNegativeButton("Eliminar") { _, _ ->
                        eliminar()
                    }.show()
                return@setOnLongClickListener true
            }
        }

        private fun editar() {
            Toast.makeText(itemView.context, "En construccion", Toast.LENGTH_SHORT).show()
        }

        private fun eliminar() {
            MaterialAlertDialogBuilder(itemView.context)
                .setTitle("Eliminar problema")
                .setMessage("¿Seguro que quiere eliminar el problema?")
                .setPositiveButton("Eliminar") { _, _ ->
                    deleteAndUpdateUI()
                }.setNegativeButton("Cancelar") { _, _ ->
                }.show()
        }

        private fun deleteAndUpdateUI() {
            items.removeAt(adapterPosition)
            ReporteActivity.fallaAdapter = FallaAdapter()
            ReporteActivity.fallaAdapter.submitList(items)
            ReporteActivity.recyclerMain.apply {
                layoutManager = GridLayoutManager(itemView.context, 1)
                adapter = ReporteActivity.fallaAdapter
            }
        }

        fun bind(falla: Falla) {
            fallaTxt.text = falla.getFalla()
            descrTxt.text = falla.getFallaDesc()
            itemImagen.setImageURI(Uri.parse(items[adapterPosition].getUris()[0]))
        }
    }
}

