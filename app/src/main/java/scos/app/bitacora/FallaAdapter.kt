package scos.app.bitacora

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import scos.app.bitacora.forms.FormProblemaActivity

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
        private val fechaTxt: TextView = itemView.findViewById(R.id.fecha)
        private val tecnicoTxt: TextView = itemView.findViewById(R.id.tecnicoTxt)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                val intent = Intent(itemView.context, FormProblemaActivity::class.java)
                intent.apply {
                    putExtra("falla", items[position])
                }
                itemView.context.startActivity(intent)
            }
            itemView.setOnLongClickListener {
                val position: Int = adapterPosition
                MaterialAlertDialogBuilder(itemView.context).setTitle(
                    "Falla: " + fallaTxt.text.toString().toUpperCase()
                ).setMessage("Que desea hacer?").setNeutralButton("Ver") { _, _ ->
                    //En construcción
                }.setPositiveButton("editar") { _, _ ->
                    //En construcción
                }.setNegativeButton("elminar") { _, _ ->
                    //En construcción
                }.show()
                return@setOnLongClickListener true
            }
        }

        fun bind(falla: Falla) {

            val meses = listOf(
                "Enero,",
                "Febrero",
                "Marzo",
                "Abril",
                "Mayo",
                "Junio",
                "Julio",
                "Agosto",
                "Septiembre",
                "Octubre",
                "Noviembre",
                "Diciembre"
            )

            val hora = falla.getHora() + ":" + falla.getMin() + " "
            val fecha = falla.getDia() + "/" + meses[falla.getMes().toInt()] + "/" + falla.getAnio()
            fallaTxt.text = falla.getFalla()
            fechaTxt.text = hora + fecha
            tecnicoTxt.text = "Técnico/Ingeniero: " + falla.getTecnico()
        }
    }
}