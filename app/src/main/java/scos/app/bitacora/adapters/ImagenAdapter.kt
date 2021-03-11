package scos.app.bitacora.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import scos.app.bitacora.R
import java.util.*

class ImagenAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var items: MutableList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImagenViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_imagen,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImagenViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(imagenes: List<String>) {
        items = ArrayList(imagenes)
    }

    inner class ImagenViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagen: ImageView = itemView.findViewById(R.id.imagen)
        fun bind(imagen: String) {
            this.imagen.setImageURI(Uri.parse(imagen))
        }
    }
}