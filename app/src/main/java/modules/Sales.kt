package modules

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Sales(val uid: String, val fullnames :String, val profileImageUrl:String, val email:String, val postcode:String,val homename:String, val location:String, val propertytype:String, val rooms:String, val bedrooms:String, val bathrooms:String, val furnishing:String, val price1:String, val price2:String, val abouthome:String, val image1:String, val image2:String, val image3:String,val timestamp:String):
    Parcelable {
    constructor() : this("","","","","","","","","","","","","","","","","","","")
}