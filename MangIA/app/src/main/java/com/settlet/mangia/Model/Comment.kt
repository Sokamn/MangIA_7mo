package com.settlet.mangia.Model

data class Comment (
               internal var commentID:String = "",
               internal var comment: String = "",
               internal var publisher: String = "",
               internal var likes: Int = 0,
               internal var timeLaunch: String = ""
               ){

}
