package modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Request(val uid: String, val fullname:String, val profilepic:String, val toid:String, val timestamp:String,val property:String, val mphone:String, val memail:String):
    Parcelable {
    constructor() : this("","","","","","","","")
}