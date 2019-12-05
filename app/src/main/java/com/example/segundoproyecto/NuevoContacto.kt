package com.example.segundoproyecto

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.contenido.imageViewContacto
import kotlinx.android.synthetic.main.nuevo_contacto.*
import java.io.IOException
import java.net.URL
import android.content.Context.MODE_PRIVATE
import java.io.FileNotFoundException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import android.graphics.drawable.BitmapDrawable
import java.io.FileInputStream
import android.R.attr.bitmap




class NuevoContacto : AppCompatActivity() {

    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    var urlImagen = ""
    var btm: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nuevo_contacto)

        imageViewContacto.setOnClickListener{selectImageInAlbum()}

    }


    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == RESULT_OK) {
            val imageUri = data!!.data
            btm = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            imageView2.setImageBitmap(btm)

            urlImagen = imageUri.toString().substring(63)+ ".gif"

        }
    }

    fun insertar(v : View){
        if ((etNombre.length()>0) and (etNumero.length()>0)) {

            val stream = ByteArrayOutputStream()
            btm?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            try {
                val outputStream = applicationContext.openFileOutput(urlImagen,Context.MODE_PRIVATE)
                outputStream.write(byteArray)
                outputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e2: IOException) {
                e2.printStackTrace()
            }

            val url = "http://iesayala.ddns.net/albertomendoza/php/insertarcontacto.php/?Nombre=" + etNombre.text + "&Tlfn=" + etNumero.text + "&IMG=" + urlImagen

            //Ejecutar la url
            var policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val response = try {
                URL(url)
                    .openStream()
                    .bufferedReader()
                    .use { it.readText() }
            } catch (e: IOException) {
                Toast.makeText(this, "Error al guardar contacto",Toast.LENGTH_LONG).show()
            }
            Toast.makeText(this, "Contacto Guardado",Toast.LENGTH_LONG).show()

            finish()

        }else{
            Toast.makeText(this, "Debes poner un nombre y un numero",Toast.LENGTH_LONG).show()
        }
    }

}
