package metri.amit.sampleapp.repository;

import java.util.List;

import io.reactivex.Single;
import metri.amit.sampleapp.model.Country;
import metri.amit.sampleapp.model.Province;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by amitmetri on 04,April,2021
 */
interface CountryServices {

    @GET("rest/worldregions/country")
    Single<List<Country>> getCountriesList();

    @GET("rest/worldregions/country/{ID}/province")
    Single<List<Province>> getProvinceList(@Path("ID") String countryId);
}
