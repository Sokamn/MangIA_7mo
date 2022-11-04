package com.settlet.mangia.Model

data class Message (
    var messageID: String? = null,
    var message: String? = null,
    var senderID: String? = null,
    var imageUrl: String? = null,
    var timeStamp: Long = 0,
    var hour: String? = null,
    var seen:Boolean = false
){
}
