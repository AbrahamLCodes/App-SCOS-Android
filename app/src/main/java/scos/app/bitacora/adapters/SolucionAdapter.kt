package scos.app.bitacora.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import scos.app.bitacora.R
import scos.app.bitacora.dialogs.SolucionDialogCustom
import scos.app.bitacora.forms.FormSolucionActivity
import scos.app.bitacora.modelos.Solucion

class SolucionAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var items: MutableList<Solucion>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SolucionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_detalle,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SolucionAdapter.SolucionViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class SolucionViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            SolucionDialogCustom(adapterPosition, items[adapterPosition])
                .show(supportFragmentManager(), "Editar falla")
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
            FormSolucionActivity.fallasList.removeAt(adapterPosition)
            FormSolucionActivity.fallaAdapter = FallaAdapter()
            FormSolucionActivity.fallaAdapter.submitList(FormSolucionActivity.fallasList)
            FormSolucionActivity.recyclerMain.apply {
                layoutManager = GridLayoutManager(itemView.context, 1)
                adapter = FormSolucionActivity.fallaAdapter
            }
        }

        private fun supportFragmentManager(): FragmentManager {
            return (itemView.context as FragmentActivity).supportFragmentManager
        }

        fun bind(solucion: Solucion) {
            fallaTxt.text = solucion.getSolucion()
            descrTxt.text = solucion.getSolucionDescripcion()
            itemImagen.setImageURI(Uri.parse(items[adapterPosition].getUris()[0]))
        }
    }

}