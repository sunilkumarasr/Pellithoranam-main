package com.stranger_sparks.inerfaces

import com.stranger_sparks.data_model.GalleryImagesResponse

interface OnItemClickListenerProfilesGalleryImages {
    fun clickOnCurrentPositionListener(item: GalleryImagesResponse.Data, position: Int)
}