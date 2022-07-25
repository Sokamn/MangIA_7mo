package com.settlet.mangia.Model

import android.net.Uri

data class Step(internal var nStep:Int = 0, internal var expandable:Boolean = false, internal var optionalImage:String?=null ) {
    var sDescription:String = ""
}