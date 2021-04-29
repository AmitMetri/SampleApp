package metri.amit.sampleapp.model

import java.io.Serializable

/*
* Model class for Country List
* Improvement can be done here with Parcelable implementation
* */
data class Country(
        var ID: Int?,
        var Name: String?,
        var Code: String?,
        var PhoneCode: String?
) : Serializable