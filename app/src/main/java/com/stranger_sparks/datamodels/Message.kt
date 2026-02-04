package com.stranger_sparks.datamodels

class Message {
    var messageId: String? = ""
    var message: String? = ""
    var senderId: String? = ""
    var imageUrl: String? = ""
    var timeStamp: Long = 0

    constructor(){}
    constructor(message: String?,senderId: String?, timeStamp: Long){
        this.message = message
        this.senderId = senderId
        this.timeStamp = timeStamp
    }
}