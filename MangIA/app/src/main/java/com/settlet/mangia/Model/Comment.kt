package com.settlet.mangia.Model

data class Comment (
                var commentID:String = "",
                var comment: String = "",
                var publisher: String = "",
                var likes: Int = 0,
                var timeLaunch: String = "",
                var cantComments: Int = 0,
                var recipeID: String = ""
               ){

}
