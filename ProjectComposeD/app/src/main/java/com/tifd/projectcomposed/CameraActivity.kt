package com.tifd.projectcomposed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : ComponentActivity() {

    private lateinit var imageCapture: ImageCapture
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    companion object {

        const val EXTRA_CAMERAX_IMAGE = "camerax_image"
        private const val TAG = "CameraActivity"
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera) // Ensure you have a layout file with this name

        startCamera()
        setupCaptureButton()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(findViewById<PreviewView>(R.id.viewFinder).surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(this, "Gagal memunculkan kamera.", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun setupCaptureButton() {
        findViewById<Button>(R.id.captureButton).setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        val photoFile = createCustomTempFile()

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent().apply {
                        putExtra(EXTRA_CAMERAX_IMAGE, output.savedUri.toString())
                    }
                    setResult(RESULT_OK, intent)  // Use RESULT_OK here
                    finish() // Close the activity
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(this@CameraActivity, "Gagal mengambil gambar.", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun createCustomTempFile(): File {
        val filesDir = externalCacheDir ?: throw IllegalStateException("External cache directory is not available.")
        val timeStamp = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }
}
