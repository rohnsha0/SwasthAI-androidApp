package com.rohnsha.dermbuddyai.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class analyzer(
    private val context: Context,
    private val onResults: (classification) -> Unit
): ImageAnalysis.Analyzer {

    private var frameSkipCounter= 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter%60==0){
            val matrix = Matrix().apply {
                postRotate(image.imageInfo.rotationDegrees.toFloat())
            }
            val rotatedBitmap = Bitmap.createBitmap(
                image.toBitmap(),
                0,
                0,
                image.width,
                image.height,
                matrix,
                true
            )
            val results= classifier.classifyIndex(context, bitmap = rotatedBitmap)
            onResults(results)
            Log.d("analyzerSuccess", results.indexNumber.toString())
        }
        frameSkipCounter++
        image.close()
    }


}