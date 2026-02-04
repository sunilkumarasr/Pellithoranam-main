package com.stranger_sparks.inerfaces

import com.stranger_sparks.data_model.ChatResponse
import com.stranger_sparks.data_model.UserProfileResponse

interface OnItemClickListenerChatRoom {
    fun clickOnCurrentPositionListener(item: ChatResponse.Data,type: String)
}