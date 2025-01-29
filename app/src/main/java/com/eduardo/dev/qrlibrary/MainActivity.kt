package com.eduardo.dev.qrlibrary

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eduardo.dev.qrlibrary.ui.theme.QrLibraryTheme

class MainActivity : ComponentActivity() {
    private var selectedColor by mutableStateOf(Color.Black)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val qrGenerator = QrGenerator()
        val logoBitmap = BitmapFactory.decodeResource(resources, R.drawable.asp)

        setContent {
            var showColorPicker by remember { mutableStateOf(false) }

            QrLibraryTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { showColorPicker = true }) {
                        Text("Seleccionar Color")
                    }

                    if (showColorPicker) {
                        ColorPickerDialog(
                            onColorSelect = {
                                selectedColor = it
                                showColorPicker = false
                            },
                            onDismiss = { showColorPicker = false }
                        )
                    }

                    val qrBitmap = qrGenerator.generateQr(
                        content = "https://www.eduardodev.com",
                        qrColor = selectedColor.toArgb(),
                        logo = logoBitmap
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = stringResource(id = R.string.qr_code),
                        modifier = Modifier.size(250.dp)
                    )
                }
            }
        }
    }
}

