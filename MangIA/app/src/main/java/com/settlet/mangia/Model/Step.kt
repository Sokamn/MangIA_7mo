package com.settlet.mangia.Model

import android.net.Uri

data class Step(internal var nStep:Int, internal var expandable:Boolean = false, internal var optionalImage:Uri?=null ) {
    var sDescription:String = ""
}