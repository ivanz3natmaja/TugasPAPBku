package com.tifd.projectcomposed

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.material.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.combinedClickable
import com.tifd.projectcomposed.ui.theme.ProjectComposeDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjectComposeDTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InputOutputApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputOutputApp() {
    var inputText by remember { mutableStateOf("") }
    var secondInputText by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var showText by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val isForaFilled by remember { derivedStateOf { inputText.isNotEmpty() && secondInputText.isNotEmpty() } }

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text(text = "Masukkan nama") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Icon Profile",
                        tint = Color.Black,
                        modifier = Modifier.size(25.dp)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(90.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = secondInputText,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        secondInputText = it
                    }
                },
                label = { Text(text = "Masukkan NIM") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Icon Profile",
                        tint = Color.Black,
                        modifier = Modifier.size(25.dp)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(90.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                text = "$inputText\n$secondInputText"
                showText = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { text = "$inputText\n$secondInputText"
                        showText = true },
                    onLongClick = {
                        // Show a toast with name and NIM on long click
                        Toast.makeText(context, "Nama: $inputText, NIM: $secondInputText", Toast.LENGTH_SHORT).show()
                    }
                ),
            enabled = isForaFilled,
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(text = "Submit")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showText) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }
        }
    }
}
