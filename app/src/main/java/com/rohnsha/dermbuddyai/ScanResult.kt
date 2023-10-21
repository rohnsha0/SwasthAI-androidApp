package com.rohnsha.dermbuddyai

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun ScanResultScreen(
    padding: PaddingValues,
    group: String,
    serial_number: String,
) {
    Column {
        Text(text = group)
        Text(text = serial_number)
    }
}