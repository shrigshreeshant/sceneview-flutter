package io.github.sceneview.sceneview_flutter

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.PixelCopy
import android.view.SurfaceView
import androidx.core.graphics.createBitmap

object ARPhotoSaver {

    fun takePhoto(
        context: Context,
        view: SurfaceView,
        onResult: (Boolean) -> Unit // Callback to return success or failure
    ) {
        Log.d("ARPhotoSaver", "Attempting to capture photo...")

        val bitmap = createBitmap(view.width, view.height)

        PixelCopy.request(
            view,
            bitmap,
            { copyResult ->
                Log.d("ARPhotoSaver", "PixelCopy result: $copyResult")
                if (copyResult == PixelCopy.SUCCESS) {
                    Log.d("ARPhotoSaver", "PixelCopy succeeded. Proceeding to save...")
                    val success = saveBitmapToGallery(context, bitmap)
                    onResult(success)
                    if (success) {
                        Log.d("ARPhotoSaver", "Photo saved to gallery successfully.")
                    } else {
                        Log.e("ARPhotoSaver", "Failed to save photo to gallery.")
                    }
                } else {
                    Log.e("ARPhotoSaver", "PixelCopy failed with code: $copyResult")
                    onResult(false)
                }
            },
            Handler(Looper.getMainLooper())
        )
    }

    private fun saveBitmapToGallery(context: Context, bitmap: Bitmap): Boolean {
        val filename = "AR_Image_${System.currentTimeMillis()}.jpg"
        Log.d("ARPhotoSaver", "Saving image with filename: $filename")

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyARPhotos")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        return if (uri != null) {
            try {
                Log.d("ARPhotoSaver", "Created MediaStore entry: $uri")
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    Log.d("ARPhotoSaver", "Bitmap compress result: $saved")
                    if (!saved) return false
                } ?: run {
                    Log.e("ARPhotoSaver", "Output stream was null")
                    return false
                }

                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)

                Log.d("ARPhotoSaver", "MediaStore entry updated. Image visible in gallery.")
                true
            } catch (e: Exception) {
                Log.e("ARPhotoSaver", "Error saving image: ${e.message}", e)
                false
            }
        } else {
            Log.e("ARPhotoSaver", "Failed to create MediaStore URI")
            false
        }
    }
}
