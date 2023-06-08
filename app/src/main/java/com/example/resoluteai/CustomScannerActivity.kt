package com.example.resoluteai

import android.content.pm.ActivityInfo
import com.journeyapps.barcodescanner.CaptureActivity

class CustomScannerActivity : CaptureActivity() {
    override fun getRequestedOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}