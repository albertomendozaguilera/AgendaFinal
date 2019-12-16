package com.example.segundoproyecto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.contenido.view.*
import kotlinx.android.synthetic.main.contenido.view.imageViewContacto
import java.io.*
import java.io.FileInputStream




class Adaptador (var contactos : ArrayList<Contacto>, val context: Context, val clickListener: (Contacto) -> Unit):RecyclerView.Adapter<Adaptador.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.contenido, parent, false))
    }

    override fun getItemCount(): Int {
        return contactos.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s: String = context.filesDir.path
        holder.enlazaItems(contactos[position], s, clickListener)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {




        fun enlazaItems(datos:Contacto, s: String, clickListener: (Contacto) -> Unit){
            val tvNombre = itemView.tvDireccion
            val tvNumero = itemView.tvNumero
            val img = itemView.imageViewContacto

            itemView.setOnClickListener { clickListener(datos)}

            tvNombre.text=datos.nombre
            tvNumero.text=datos.numero

            var bitmap: Bitmap? = null
            try {
                val fileInputStream = FileInputStream(s + "/"+datos.img)
                bitmap = BitmapFactory.decodeStream(fileInputStream)

            } catch (io: IOException) {
                io.printStackTrace()
            }
            img.setImageBitmap(bitmap)


        }

    }



}
