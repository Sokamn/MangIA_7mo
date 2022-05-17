package com.settlet.mangia

import java.util.*

data class User (var email: String = "",
    var phoneNumber: String = "",
    var userName: String = "",
    var nickName: String = "",
    var country: String = "" ,
    var region: String = "",
    var dateBirth: Date = Date(0,0,0),
    var dateCreationAccount: Date = Date(0,0,0),
    var password: String = "",
    var cantReports: Int = 0,
    var age: Int = 0,
    var cantFollows: Int = 0,
    var cantFollowers: Int = 0,
    var biography: String = "") {

}

