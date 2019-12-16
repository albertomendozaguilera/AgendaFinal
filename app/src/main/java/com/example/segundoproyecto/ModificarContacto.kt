package com.example.segundoproyecto

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.view.View
import kotlinx.android.synthetic.main.contenido.*
import kotlinx.android.synthetic.main.modificar_contacto.*
import kotlinx.android.synthetic.main.nuevo_contacto.*
import kotlinx.android.synthetic.main.nuevo_contacto.imageViewContacto
import kotlinx.android.synthetic.main.review_contacto.*
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL

class ModificarContacto : AppCompatActivity() {

    var id = 0
    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    var urlImagen = ""
    var btm: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modificar_contacto)

        val c:Contacto = intent.getSerializableExtra("contacto") as Contacto
        id = c.id

        urlImagen = c.img

        etModContacto.text.insert(0, c.nombre)
        etModNumero.text.insert(0, c.numero)
        etModDireccion.text.insert(0, c.direccion)

        var bitmap: Bitmap? = null
        try {
            val fileInputStream = FileInputStream(this.filesDir.path + "/"+c.img)
            bitmap = BitmapFactory.decodeStream(fileInputStream)

        } catch (io: IOException) {
            io.printStackTrace()
        }
        ivModImagen.setImageBitmap(bitmap)

        ivModImagen.setOnClickListener{selectImageInAlbum()}
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
            ivModImagen.setImageBitmap(btm)

            urlImagen = imageUri.toString().substring(63)+ ".gif"

        }
    }


    fun guardar(v:View){
        val stream = ByteArrayOutputStream()
        btm?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        try {
            val outputStream = applicationContext.openFileOutput(urlImagen, Context.MODE_PRIVATE)
            outputStream.write(byteArray)
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e2: IOException) {
            e2.printStackTrace()
        }

        val url = "http://iesayala.ddns.net/albertomendoza/php/modificarcontacto.php/?ID="+ id +"&Nombre=" + etModContacto.text + "&Tlfn=" + etModNumero.text + "&IMG="+ urlImagen + "&direccion=" + etModDireccion.text
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
    }
}
