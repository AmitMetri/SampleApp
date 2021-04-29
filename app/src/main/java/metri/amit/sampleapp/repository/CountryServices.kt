package metri.amit.sampleapp.repository

import io.reactivex.Observable
import io.reactivex.Single
import metri.amit.sampleapp.model.Country
import metri.amit.sampleapp.model.Province
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by amitmetri on 28,April,2021
 * Service class for retrofit
 */
internal interface CountryServices {
    /*@get:GET("rest/worldregions/country")
    val countriesList: Single<List<Country?>?>?*/

    @GET("rest/worldregions/country")
    fun getCountryList():  Observable<List<Country?>?>

    @GET("rest/worldregions/country/{ID}/province")
    fun getProvinceList(@Path("ID") countryId: String?): Observable<List<Province?>?>
}