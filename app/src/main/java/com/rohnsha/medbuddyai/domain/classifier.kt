package com.rohnsha.medbuddyai.domain

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.rohnsha.medbuddyai.domain.dataclass.classification
import com.rohnsha.medbuddyai.ml.ModelImgClf
import com.rohnsha.medbuddyai.ml.PneumoniaV3
import com.rohnsha.medbuddyai.ml.TuberculosisV1
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
            scanOption: Int,
        ): List<classification> {
            var predictedClasses:List<classification> = emptyList()

            var tensorImage= TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            var imageProcessor= ImageProcessor.Builder()
                .add(ResizeOp(256, 256, ResizeOp.ResizeMethod.BILINEAR))
                .build()
            tensorImage= imageProcessor.process(tensorImage)
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 256, 256, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)

            when(scanOption){
                0 -> { predictedClasses=predictionLungs(context, inputFeature = inputFeature0, bitmap) }
                1 -> { predictedClasses=predictionHeart() }
                2 -> { predictedClasses= predictionBrain() }
                3 -> { predictionSkin() }
                4 -> { predictionLimbs() }
                5 -> { predictedClasses=predictionMaster(context = context, inputFeature0 = inputFeature0, bitmap = bitmap) }
            }
            return predictedClasses
        }

        fun predictionMaster(
            bitmap: Bitmap,
            inputFeature0: TensorBuffer,
            context: Context
        ): List<classification>{
            val predictedClassesLungs= mutableListOf<classification>()

            val modelPneumonia= ModelImgClf.newInstance(context)
            val outputPneumonia= modelPneumonia.process(inputFeature0).outputFeature0AsTensorBuffer
            val maxIndexPneumonia= getMaxIndex(outputPneumonia.floatArray)
            predictedClassesLungs.add(
                classification(
                    indexNumber = maxIndexPneumonia,
                    confident = outputPneumonia.floatArray[getMaxIndex(outputPneumonia.floatArray)]*100,
                    parentIndex = 0
                ),
            )
            return predictedClassesLungs
        }

        fun predictionLimbs(
        ): List<classification>{
            val predictedClassesLungs= mutableListOf<classification>()
            return predictedClassesLungs
        }

        fun predictionSkin(
        ): List<classification>{
            val predictedClassesLungs= mutableListOf<classification>()
            return predictedClassesLungs
        }

        fun predictionBrain(
        ): List<classification>{
            val predictedClassesLungs= mutableListOf<classification>()
            return predictedClassesLungs
        }
        fun predictionHeart(
        ): List<classification>{
            val predictedClassesLungs= mutableListOf<classification>()

            predictedClassesLungs.add(
                classification(
                    indexNumber = 1,
                    confident = 0.98f*100,
                    parentIndex = 1
                ),
            )

            return predictedClassesLungs
        }

        fun predictionLungs(
            context: Context,
            inputFeature: TensorBuffer,
            bitmap: Bitmap
        ): List<classification> {
            val predictedClassesLungs= mutableListOf<classification>()

            val modelPneumonia= PneumoniaV3.newInstance(context)
            var tensorImage= TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            var imageProcessor= ImageProcessor.Builder()
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
                .build()
            tensorImage= imageProcessor.process(tensorImage)
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(tensorImage.buffer)
            val outputPneumonia= modelPneumonia.process(inputFeature0).outputFeature0AsTensorBuffer
            val maxIndexPneumonia= getMaxIndex(outputPneumonia.floatArray)
            predictedClassesLungs.add(
                classification(
                    indexNumber = maxIndexPneumonia,
                    confident = outputPneumonia.floatArray[getMaxIndex(outputPneumonia.floatArray)]*100,
                    parentIndex = 0
                ),
            )
            Log.d("successIndex", predictedClassesLungs.toString())

            val modelTuberCulosis= TuberculosisV1.newInstance(context)
            val outputTuberculosis= modelTuberCulosis.process(inputFeature).outputFeature0AsTensorBuffer
            val maxIndexTuberCulosis= getMaxIndex(outputTuberculosis.floatArray)
            predictedClassesLungs.add(
                classification(
                    indexNumber = maxIndexTuberCulosis,
                    confident = outputTuberculosis.floatArray[getMaxIndex(outputTuberculosis.floatArray)]*100,
                    parentIndex = 1
                )
            )
            modelPneumonia.close()
            modelTuberCulosis.close()
            return predictedClassesLungs
        }

        fun getMaxIndex(floatArray: FloatArray): Int {
            return floatArray.withIndex().maxByOrNull { it.value }?.index ?: -1
        }
    }

}