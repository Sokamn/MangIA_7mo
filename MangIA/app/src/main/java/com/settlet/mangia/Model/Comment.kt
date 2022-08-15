package com.settlet.mangia.Model

data class Comment (
                var commentID:String = "",
                var comment: String = "",
                var publisher: String = "",
                var timeLaunch: String = "",
                var recipeID: String = "",
                var answerID: String? = "",
                var opened: Boolean = false
               ){

}
