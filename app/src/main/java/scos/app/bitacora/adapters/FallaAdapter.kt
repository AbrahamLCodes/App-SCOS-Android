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
import scos.app.bitacora.forms.FallaDialogCustom
import scos.app.bitacora.forms.ReporteActivity
import scos.app.bitacora.modelos.Falla


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
            FallaDialogCustom(adapterPosition, items[adapterPosition])
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
            ReporteActivity.fallasList.removeAt(adapterPosition)
            ReporteActivity.fallaAdapter = FallaAdapter()
            ReporteActivity.fallaAdapter.submitList(ReporteActivity.fallasList)
            ReporteActivity.recyclerMain.apply {
                layoutManager = GridLayoutManager(itemView.context, 1)
                adapter = ReporteActivity.fallaAdapter
            }
        }

        private fun supportFragmentManager(): FragmentManager {
            return (itemView.context as FragmentActivity).supportFragmentManager
        }

        fun bind(falla: Falla) {
            fallaTxt.text = falla.getFalla()
            descrTxt.text = falla.getFallaDesc()
            itemImagen.setImageURI(Uri.parse(items[adapterPosition].getUris()[0]))
        }
    }
}

