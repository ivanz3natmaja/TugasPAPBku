package com.tifd.projectcomposed
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListActivityContent()
        }
    }
}


@Composable
fun GithubFloatingActionButton() {
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https://github.githubassets.com/assets/GitHub-Mark-ea2971cee799.png")
            .build()
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .size(150.dp) // Mengatur ukuran gambar
                .padding(16.dp)
                .align(Alignment.TopEnd) // Memposisikan gambar di pojok kanan atas
                .clickable {
                    val intent = Intent(context, GithubProfile::class.java)
                    context.startActivity(intent)
                    Toast.makeText(context, "Clicked on GitHub Logo", Toast.LENGTH_SHORT).show()
                },
            painter = painter,
            contentDescription = null // Bisa diisi deskripsi gambar
        )

        if (painter.state is AsyncImagePainter.State.Loading) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(Color.Red)
                    .align(Alignment.TopEnd) // Memposisikan Box loading di pojok kanan atas
            )
        }
    }
}




@Composable
fun ListActivityContent() {
    Scaffold(
        floatingActionButton = { GithubFloatingActionButton() }
    ) { innerPadding ->
        FirestoreDataDisplay(modifier =
        Modifier.padding
            (innerPadding))
    }
}


@Composable
fun FirestoreDataDisplay(modifier: Modifier = Modifier) {
    val db = Firebase.firestore
    var dataList by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    LaunchedEffect(Unit) {
        val result = db.collection("matkul").get().await()
        dataList = result.documents.map { it.data ?: emptyMap() }
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
                        .fillMaxWidth(),horizontalAlignment = Alignment.Start
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