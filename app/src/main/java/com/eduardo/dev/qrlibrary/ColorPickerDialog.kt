package com.eduardo.dev.qrlibrary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerDialog(onColorSelect: (Color) -> Unit, onDismiss: () -> Unit) {
    val controller = rememberColorPickerController()
    var selectedColor by remember { mutableStateOf(Color.Black) }

    BasicAlertDialog(onDismissRequest = onDismiss, content = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Selecciona un color", fontWeight = FontWeight.Bold)

            HsvColorPicker(modifier = Modifier.size(250.dp),
                controller = controller,
                onColorChanged = { colorEnvelope -> selectedColor = colorEnvelope.color })
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(selectedColor, shape = CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
            TextButton(onClick = { onColorSelect(selectedColor); onDismiss() }) {
                Text("Seleccionar", color = Color.Black)
            }
        }
    })
}


@Preview(showSystemUi = true)
@Composable
fun ColorPickerDialogPreview() {
    ColorPickerDialog(onColorSelect = {}, onDismiss = {})
}