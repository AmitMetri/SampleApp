package metri.amit.sampleapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
* Model class for Province list
* Improvement can be done here with Parcelable implementation
* */
class Province {
    var ID: Int? = null
    var CountryCode: String? = null
    var Code: String? = null
    var Name: String? = null
}