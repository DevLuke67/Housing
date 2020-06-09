package modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val fullnames: String,val email:String, val profileImageUrl: String, val bio : String, val phone: String, val location :String,val userid:String,val website: String, val gender:String, val organisation:String):
    Parcelable {
    constructor() : this("","","","","","","","","","","")
}