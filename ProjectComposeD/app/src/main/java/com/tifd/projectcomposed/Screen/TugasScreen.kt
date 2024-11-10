package com.tifd.projectcomposed.Screen

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.tifd.projectcomposed.data.model.MainViewModel
import com.tifd.projectcomposed.data.model.MainViewModelFactory
import com.tifd.projectcomposed.local.TugasRepository
import com.tifd.projectcomposed.CameraActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"

@OptIn(ExperimentalPermissionsApi::class) // Enable experimental API usage
@Composable
fun TugasScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val tugasRepository = TugasRepository(application)
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(tugasRepository))
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var matkul by remember { mutableStateOf("") }
    var detailTugas by remember { mutableStateOf("") }
    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var currentImageUri by remember { mutableStateOf<String?>(null) }

    val tugasList by mainViewModel.tugasList.observeAsState(emptyList())

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentImageUri?.let { uriString ->
                val uri = Uri.parse(uriString) // Convert string to Uri
                bitmap = loadBitmapFromUri(context, uri)
            }
        } else {
            Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val newImageUri = createImageFile(context) // Assuming createImageFile returns Uri?
            currentImageUri = newImageUri?.toString() // Store as String if needed
            newImageUri?.let { uri ->
                cameraLauncher.launch(uri) // Pass Uri to cameraLauncher
            }
        } else {
            Toast.makeText(context, "Camera permission is required to take a photo.", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(REQUIRED_PERMISSION)
    )

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    fun showSnackbar(message: String) {
        snackbarMessage = message
        snackbarVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Tambah Tugas", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = matkul,
                onValueChange = { matkul = it },
                label = { Text("Matkul") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = detailTugas,
                onValueChange = { detailTugas = it },
                label = { Text("Detail Tugas") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { startCameraX(context) }) {
                    Text(text = "Buka Kamera X")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { startGallery(context) }) {
                    Text(text = "Buka Galeri")
                }
            }

            Button(
                onClick = {
                    if (matkul.isNotEmpty() && detailTugas.isNotEmpty()) {

                        mainViewModel.addTugas(matkul = matkul, detailTugas = detailTugas, currentImageUri)
                        showSnackbar("Tugas berhasil ditambahkan")
                        matkul = ""
                        detailTugas = ""
                        bitmap = null
                        currentImageUri = null
                    } else {
                        showSnackbar("Isi semua kolom!")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Tambah Tugas")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(tugasList) { tugas ->
                TaskItem(tugas.matkul, tugas.detailTugas, tugas.imageUri)
            }
        }

        if (snackbarVisible) {
            Snackbar(
                action = {
                    TextButton(onClick = { snackbarVisible = false }) {
                        Text("Tutup")
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(snackbarMessage)
            }
        }
    }

    val launcherIntentCameraX = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
            if (imageUri != null) {
                currentImageUri = imageUri
                showImage(context, currentImageUri)
            } else {
                Log.e("TugasScreen", "Image URI is null")
            }
        } else {
            Log.e("TugasScreen", "Camera activity result was not OK")
        }
    }

}

@Composable
fun TaskItem(matkul: String, detailTugas: String, imageUri: String?) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Mata Kuliah: $matkul", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Detail: $detailTugas", fontSize = 14.sp)
            imageUri?.let { uri ->
                val tugasBitmap = loadBitmapFromUri(context, Uri.parse(uri))
                tugasBitmap?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                }
            }

        }
    }
}

private fun startCameraX(context: Context) {
    val intent = Intent(context, CameraActivity::class.java)
    context.startActivity(intent)
}

private fun startGallery(context: Context) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    context.startActivity(intent)
}

private fun showImage(context: Context, uri: String?) {
    if (uri != null) {
        Toast.makeText(context, "Image saved at: $uri", Toast.LENGTH_SHORT).show()
    }
}

private fun createImageFile(context: Context): Uri? {
    return try {
        val file = File(context.filesDir, "tugas_image_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}