package com.example.segundoproyecto

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.review_contacto.*
import kotlinx.android.synthetic.main.review_contacto.tvDireccion
import kotlinx.android.synthetic.main.review_contacto.tvNumero
import java.io.FileInputStream
import java.io.IOException
import java.net.URL

class ReviewContacto : AppCompatActivity(){

    var contacto:Contacto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.review_contacto)

        val c:Contacto = intent.getSerializableExtra("contacto") as Contacto
        contacto = c

        tvNombre.text = c.nombre
        tvNumero.text = c.numero
        tvDireccion.text = c.direccion

        if (c.favorito.equals("S")) {
            switchFavorito.isChecked = true
        }

        var bitmap: Bitmap? = null
        try {
            val fileInputStream = FileInputStream(this.filesDir.path + "/"+c.img)
            bitmap = BitmapFactory.decodeStream(fileInputStream)

        } catch (io: IOException) {
            io.printStackTrace()
        }
        imagen.setImageBitmap(bitmap)


        tvNumero.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvNumero.text))
            startActivity(intent)
        })

        tvDireccion.setOnClickListener(View.OnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            val c = tvDireccion.text
            i.data = Uri.parse("geo:0,0?q=+" + c!!)
            startActivity(i)
        })


        val sw:Switch = findViewById(R.id.switchFavorito)
        sw?.setOnCheckedChangeListener({ _ , isChecked ->
            var url = ""
            if (isChecked) {
                url = "http://iesayala.ddns.net/albertomendoza/php/favorito.php/?ID=" + c.id
            }else{
                url = "http://iesayala.ddns.net/albertomendoza/php/nofavorito.php/?ID=" + c.id
            }
            //Ejecutar la url
            var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val response = try {
                URL(url)
                    .openStream()
                    .bufferedReader()
                    .use { it.readText() }
            } catch (e: IOException) {}
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_contacto, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.menuBorrar) {
            var url = "http://iesayala.ddns.net/albertomendoza/php/borrarcontacto.php/?ID=" + contacto?.id
            //Ejecutar la url
            var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val response = try {
                URL(url)
                    .openStream()
                    .bufferedReader()
                    .use { it.readText() }
            } catch (e: IOException) {}
            finish()
            return true
        }
        if (id == R.id.menuModificar) {
            val intent = Intent(this, ModificarContacto::class.java)
            intent.putExtra("contacto", contacto)
            this.finish()
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
