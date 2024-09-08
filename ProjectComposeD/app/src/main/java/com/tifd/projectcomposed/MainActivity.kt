package com.tifd.projectcomposed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.tifd.projectcomposed.ui.theme.ProjectComposeDTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.*
import androidx.compose.ui.res.colorResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectComposeDTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InputOutputApp()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InputOutputApp() {
    var text by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "I Wayan Zenatmaja_225150207111020",
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 20.dp),
            color = Color(0xFF6D4C41)
        )
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Masukkan teks",color=Color(0xFFB71C1C)) }
        )
        Button(
            onClick = { outputText = text },
            modifier = Modifier.padding(top = 8.dp),
            shape =  RoundedCornerShape(10.dp)

        ) {
            Text("Tampilkan Teks",color=Color(0xFF03DAC5))
        }
        Text(
            text = outputText,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 16.dp), color=Color(0xFF388E3C)
        )
    }
}

