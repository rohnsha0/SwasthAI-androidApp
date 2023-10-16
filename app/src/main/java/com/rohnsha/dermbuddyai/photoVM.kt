package com.rohnsha.dermbuddyai

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class photoVM: ViewModel() {

    private val _bitmap= MutableStateFlow<Bitmap?>(null)
    val bitmaps= _bitmap.asStateFlow()

    fun take_photo(bitmap: Bitmap){
        _bitmap.value = bitmap
    }

}