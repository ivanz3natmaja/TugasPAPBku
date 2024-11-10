package com.tifd.projectcomposed

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun AuthScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthContent(
                onSuccessfulLogin = {
                    navController.navigate("main") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainActivityContent()
        }
    }
}

@Composable
fun AuthContent(onSuccessfulLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val auth: FirebaseAuth = Firebase.auth
    val context = LocalContext.current
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@student\\.ub\\.ac\\.id$")
    val isEmailValid by remember {
        derivedStateOf { emailRegex.matches(email) }
    }
    val isPasswordValid by remember {
        derivedStateOf { password.length >= 15 }
    }

    val isFormFilled by remember {
        derivedStateOf { email.isNotEmpty() && password.isNotEmpty() }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Authentication success.", Toast.LENGTH_SHORT).show()
                    onSuccessfulLogin()
                } else {
                    val exception = task.exception
                    val errorMessage = exception?.localizedMessage ?: "Authentication failed."
                    Toast.makeText(
                        context,
                        "Authentication failed: $errorMessage",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    isError = !isEmailValid,
                    onValueChange = { email = it },
                    label = { Text(text = "Masukkan alamat email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Icon Email",
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
                    value = password,
                    isError =!isPasswordValid,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() } && it.length <= 1000) { // For example, NIM max 1000 digits
                            password = it
                        }
                    },
                    label = { Text(text = "Masukkan Password (NIM Anda)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Icon Password",
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
                        val trimmedEmail = email.trim()
                        val trimmedPassword = password.trim()
                        signInWithEmailAndPassword(trimmedEmail, trimmedPassword)
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
    )
}


