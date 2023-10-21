package com.rohnsha.dermbuddyai.domain

import android.content.Context
import android.graphics.Bitmap
import com.rohnsha.dermbuddyai.ml.ModelPotato
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class classifier {

    companion object{
        fun classifyIndex(
            context: Context,
            bitmap: Bitmap,
        ): classification {
            val model = ModelPotato.newInstance(context)
            var tensorImage= TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)

            var imageProcessor= ImageProcessor.Builder()
                .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
                .build()

            tensorImage= imageProcessor.process(tensorImage)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            return classification(
                indexNumber = getMaxIndex(outputFeature0.floatArray),
                confident = outputFeature0.floatArray[getMaxIndex(outputFeature0.floatArray)] * 100
                )
        }

        private fun getMaxIndex(floatArray: FloatArray): Int {
            var max = 0
            for (i in floatArray.indices) {
                if (floatArray[i] > floatArray[max]) {
                    max = i
                }
            }
            return max
        }
    }

}