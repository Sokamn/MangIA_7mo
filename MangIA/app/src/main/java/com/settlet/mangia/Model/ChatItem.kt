package com.settlet.mangia.Model

data class ChatItem(
    var userID: String? = null,
    var lastMessage: String? = null,
    var unseenMessages: Int? = null,
) {
}