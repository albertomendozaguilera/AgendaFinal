package com.example.segundoproyecto

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.net.URL
import java.io.*


class MainActivity : AppCompatActivity() {

    val CONTACTO_GUARDADO = 1
    var contactos: ArrayList<Contacto> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        recycler.setLayoutManager(LinearLayoutManager(this));
        val adaptador = Adaptador(contactos, this)
        recycler.adapter = adaptador

        cargar()

        fab.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(this, NuevoContacto::class.java)
            startActivityForResult(intent, CONTACTO_GUARDADO)
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == CONTACTO_GUARDADO) {
            // Make sure the request was successful
                cargar()
        }
    }

     fun leerUrl(urlString:String): String{
        var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val response = try {
            URL(urlString)
                .openStream()
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            "Error with ${e.message}."
        }

        return response
    }

    fun cargar () {

        val gson = Gson()

        val jsonString = leerUrl("http://iesayala.ddns.net/albertomendoza/php/jsoncontactos.php")

        val contacto = gson.fromJson(jsonString, ArrayContactos::class.java)

        contactos = ArrayList <Contacto> ()
        for (c:Contacto in contacto.contactos!!.iterator()){
            contactos.add(Contacto(c.nombre, c.numero, c.img))
        }
        val adaptador = Adaptador(contactos, this)

        recycler.adapter = adaptador
    }


    fun buscar (v:View) {
        if (!etBuscar.text.isEmpty()){
            var contactosFiltrados: ArrayList<Contacto> = ArrayList()

            for (c: Contacto in contactos) {
                if (c.nombre.contains(etBuscar.text) || c.numero.contains(etBuscar.text)) {
                    contactosFiltrados.add(c)
                }
            }
            val adaptador = Adaptador(contactosFiltrados, this)

            recycler.adapter = adaptador
        }else{
            var contactosFiltrados: ArrayList<Contacto> = ArrayList()

            for (c: Contacto in contactos) {
                    contactosFiltrados.add(c)
            }
            val adaptador = Adaptador(contactosFiltrados, this)

            recycler.adapter = adaptador
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when(item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
