package com.example.resoluteai

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.resoluteai.databinding.ActivityScannerBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONException
import org.json.JSONObject

class ScannerActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private var qrScanIntegrator: IntentIntegrator? = null
    private var binding : ActivityScannerBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        firestore = FirebaseFirestore.getInstance()

        binding?.scanButton?.setOnClickListener {
            // Request camera permission if not granted
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                startScanner()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner()
            }
        }
    }

    private fun startScanner() {
        qrScanIntegrator = IntentIntegrator(this)
        qrScanIntegrator?.setOrientationLocked(false)
        qrScanIntegrator?.captureActivity = CustomScannerActivity::class.java
        qrScanIntegrator?.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "QR code scanning cancelled", Toast.LENGTH_SHORT).show()
            } else {
                val decodedValue = result.contents
                saveToFirestore(decodedValue)
                Toast.makeText(this, "QR code value: $decodedValue", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveToFirestore(value: String) {
        val document = hashMapOf("value" to value)
        firestore.collection("qr_codes")
            .add(document)
            .addOnSuccessListener { documentReference ->
                Log.e("ya daala hai",documentReference.toString())
                Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("ya galat hai",e.toString())
            }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}


