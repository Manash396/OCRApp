package com.example.ocrapp

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

object OCRUtils {

    fun recognizeTextFromImage(context: Context, uri : Uri, onResult: (String) -> Unit){
        val image = InputImage.fromFilePath(context,uri)
        val recognizer  = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener{
                visionText ->  onResult(visionText.text)
            }
            .addOnFailureListener{
                e ->
                Log.e("OCR","Failed to process ")
                onResult("Failed ")
            }

    }

    fun saveToTextFile(context: Context,text: String){
        val filename =  "Text_OCR_${System.currentTimeMillis()}.txt"
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename)
        file.writeText(text)
        Toast.makeText(context,"Saved to ${file.absolutePath} ", Toast.LENGTH_LONG).show()
    }

}