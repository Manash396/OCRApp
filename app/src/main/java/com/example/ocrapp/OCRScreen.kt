package com.example.ocrapp

import android.R
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ComponentActivity
import com.github.dhaval2404.imagepicker.ImagePicker

@Composable
fun OCRSreen(
    activity: androidx.activity.ComponentActivity
){
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var extractedText by remember{
        mutableStateOf("")
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){
          result ->
           val uri : Uri? =  result.data?.data
           uri?.let {
               OCRUtils.recognizeTextFromImage(
                   context, it
               ){
                   text -> extractedText = text
               }
           }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(colorResource(id = R.color.holo_blue_light))
        ,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text("To Text", fontSize = 23.sp, modifier = Modifier.padding(10.dp))

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                ImagePicker.with(activity)
                    .cameraOnly()
                    .crop()
                    .createIntent {
                        intent -> imagePickerLauncher.launch(intent)
                    }
            }
        ) {  Text("Take Photo") }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = extractedText.ifEmpty { "No text extracted yet." },
            color = colorResource(id = R.color.holo_orange_light),
            modifier = Modifier
                .padding(10.dp)
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .border(1.dp,colorResource(id= R.color.white))

        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),modifier = Modifier.padding(10.dp)) {
            Button(onClick = {
                OCRUtils.saveToTextFile(context, extractedText)
            }, enabled = extractedText.isNotEmpty()) {
                Text("Save as .txt")
            }

            Button(onClick = {
                clipboardManager.setText(AnnotatedString(extractedText))
            }, enabled = extractedText.isNotEmpty()) {
                Text("Clipboard")
            }
        }

    }

}