package com.settlet.mangia.Model

data class User (    var age: Int = 0,
                     var biography: String = "",
                     var cantFollowers: Int = 0,
                     var cantFollows: Int = 0,
                     var cantReports: Int = 0,
                     var cantRecipes: Int = 0,
                     var country: String = "" ,
                     var dateBirth:String =  "",
                     var dateCreationAccount: String = "",
                     var email: String = "",
                     var nickName: String = "",
                     var password: String = "",
                     var phoneNumber: String = "",
                     var region: String = "",
                     var userName: String = "") {

}

