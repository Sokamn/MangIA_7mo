package com.settlet.mangia.Model

data class Message (
    var userID: String = "",
    var lastMessage: String = "",
    var chatKey: String = "",
    var unseenMessages: Int = 0,
){

}
