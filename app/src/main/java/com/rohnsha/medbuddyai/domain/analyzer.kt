package com.rohnsha.medbuddyai.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.rohnsha.medbuddyai.domain.dataclass.classification
import com.rohnsha.medbuddyai.ml.ModelImgClf
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class analyzer(
    private val context: Context,
    private val onResults: (classification) -> Unit,
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
            val model= ModelImgClf.newInstance(context)
            var tensorImage= TensorImage(DataType.FLOAT32)
            tensorImage.load(rotatedBitmap)
            var imageProcessor= ImageProcessor.Builder()
                .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
                .build()
            tensorImage= imageProcessor.process(tensorImage)
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)
            val outputs= model.process(inputFeature0)
            val outputFeature0 =outputs.outputFeature0AsTensorBuffer
            val results= classification(
                getMaxIndex(outputFeature0.floatArray), outputFeature0.floatArray[getMaxIndex(
                outputFeature0.floatArray
            )] * 100)
            onResults(results)
            Log.d("analyzerSuccess", results.indexNumber.toString())
        }
        frameSkipCounter++
        image.close()
    }

    private fun getMaxIndex(floatArray: FloatArray?): Int {
        return floatArray?.withIndex()?.maxByOrNull { it.value }?.index ?: -1
    }


}