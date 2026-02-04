package com.stranger_sparks.inerfaces

import com.stranger_sparks.data_model.GalleryImagesResponse

interface SavedGalareyImageOnItemClickListenerWithPosition {
    fun clickOnCurrentPositionListener(position: GalleryImagesResponse.Data)
}