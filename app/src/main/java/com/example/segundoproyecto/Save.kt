package com.example.segundoproyecto

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.widget.Toast

class Save {

    private var TheThis: Context? = null
    private val NameOfFolder = "/agenda"
    var NameOfFile: String? = null

    private val currentDateAndTime: String
        get() {
            val c = Calendar.getInstance()
            val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            return df.format(c.time)
        }
    //private ArrayList<Bitmap> images = null;

    fun Save(context: Context, ImageToSave: Bitmap) {
        TheThis = context
        val file_path = Environment.getExternalStorageDirectory().absolutePath + NameOfFolder
        val CurrentDateAndTime = currentDateAndTime
        val dir = File(file_path)
        NameOfFile = "PaintImage$CurrentDateAndTime.jpg"

        if (!dir.exists()) {
            dir.mkdirs()
        }

        val file = File(dir, NameOfFile!!)
        if (file.exists()) file.delete()
        try {
            val fOut = FileOutputStream(file)
            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
            fOut.flush()
            fOut.close()
            MakeSureFileWasCreatedThenMakeAvabile(file)

        } catch (e: FileNotFoundException) {
            UnableToSave()
        } catch (e: IOException) {
            UnableToSave()
        }

    }

    private fun MakeSureFileWasCreatedThenMakeAvabile(file: File) {
        MediaScannerConnection.scanFile(
            TheThis,
            arrayOf(file.toString()), null
        ) { path, uri -> }

    }

    private fun UnableToSave() {
        Toast.makeText(TheThis, "Picture cannot to gallery", Toast.LENGTH_SHORT)
            .show()

    }
}