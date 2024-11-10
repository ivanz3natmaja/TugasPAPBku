package com.tifd.projectcomposed

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Composable
fun MatkulScreen() {
    val db = Firebase.firestore
    var dataList by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    val context = LocalContext.current

    // Check if the user is authenticated
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            // User is authenticated, proceed with fetching data
            try {
                val result = db.collection("matkul1").get().await()
                dataList = result.documents.map { it.data ?: emptyMap() }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            // User is not authenticated, notify the user
            Toast.makeText(context, "Please log in to access the data.", Toast.LENGTH_LONG).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(dataList) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE0F7FA) // Light blue color
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "📚 ${item["matkul"] ?: "Unknown"}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "📅 ${item["hari"] ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "⏰ ${item["jam"] ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📍 ${item["ruang"] ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (item["praktikum"] == true) "👨‍🔬 Praktikum" else "📖 Teori",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
