package com.tifd.projectcomposed
import android.content.Context
import android.content.Intent
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.tifd.projectcomposed.ui.theme.ProjectComposeDTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



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
    val navController = rememberNavController()
    var inputText by remember { mutableStateOf("") }
    var secondInputText by remember { mutableStateOf("") }
    var showText by remember { mutableStateOf(false) }
    val auth: FirebaseAuth = Firebase.auth

    val context = LocalContext.current

    val isFormFilled by remember { derivedStateOf { inputText.isNotEmpty() && secondInputText.isNotEmpty() } }

    fun signInWithEmailAndPassword(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, ListActivity::class.java)
                    context.startActivity(intent)
                } else {
                    // Dapatkan pesan kesalahan yang lebih spesifik
                    val exception = task.exception
                    val errorMessage = exception?.localizedMessage ?: "Authentication failed."
                    Toast.makeText(context, "Authentication failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text(text = "Masukkan alamat email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Icon Profile",
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(90.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = secondInputText,
            onValueChange = {
                if (it.all { char -> char.isDigit() } && it.length <= 1000) { // Misalnya, NIM maksimal 10 digit
                    secondInputText = it
                }
            },
            label = { Text(text = "Masukkan Password (NIM Anda)") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Icon Profile",
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(90.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val email = inputText.trim()
                val password = secondInputText.trim()
                signInWithEmailAndPassword(email, password, context)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = isFormFilled,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Submit", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

