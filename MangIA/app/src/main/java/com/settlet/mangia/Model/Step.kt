package com.settlet.mangia.Model

import android.net.Uri

class Step(internal var nStep:Int, internal var rDescription:String, internal var expandable:Boolean = false, internal var optionalImage:Uri?=null ) {

}