package com.settlet.mangia

import java.util.*

data class User (    var age: Int = 0,
                     var biography: String = "",
                     var cantFollowers: Int = 0,
                     var cantFollows: Int = 0,
                     var cantReports: Int = 0,
                     var country: String = "" ,
                     var dateBirth: Date = Date(0,0,0),
                     var dateCreationAccount: Date = Date(0,0,0),
                     var email: String = "",
                     var nickName: String = "",
                     var password: String = "",
                     var phoneNumber: String = "",
                     var region: String = "",
                     var userName: String = "") {

}

