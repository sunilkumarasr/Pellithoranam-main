package com.stranger_sparks.inerfaces

import com.stranger_sparks.data_model.UserProfileResponse

interface OnItemClickListenerProfiles {
    fun clickOnCurrentPositionListener(item: UserProfileResponse.Data)
}