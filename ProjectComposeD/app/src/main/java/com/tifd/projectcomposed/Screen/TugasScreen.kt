package com.tifd.projectcomposed.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tifd.projectcomposed.data.model.MainViewModel
import com.tifd.projectcomposed.data.model.MainViewModelFactory
import com.tifd.projectcomposed.local.TugasRepository
import android.app.Application as Application1

@Composable
fun TugasScreen(tugasRepository: TugasRepository) {
    val mainViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(tugasRepository))
    var matkul by remember { mutableStateOf("") }
    var detailTugas by remember { mutableStateOf("") }// val tugasList by mainViewModel.tugasList.observeAsState(emptyList()) // Tidak digunakan di sini
    var snackbarVisible by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

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
                modifier =Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if (matkul.isNotEmpty() && detailTugas.isNotEmpty()) {
                        mainViewModel.addTugas(matkul, detailTugas)
                        showSnackbar("Tugas berhasil ditambahkan")
                        matkul = ""
                        detailTugas = ""
                    } else {
                        showSnackbar("Isi semua kolom!")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Tambah Tugas")
            }
        }

        // Snackbar di luar Column utama
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
}