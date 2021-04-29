package metri.amit.sampleapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/*
* Model class for Country List
* Improvement can be done here with Parcelable implementation
* */
class Country : Serializable {
    public var ID: Int? = null
    public var Name: String? = null
    public var Code: String? = null
    public var PhoneCode: String? = null
}