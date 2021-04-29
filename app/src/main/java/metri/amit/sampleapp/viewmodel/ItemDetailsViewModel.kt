package metri.amit.sampleapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import metri.amit.sampleapp.model.ErrorData
import metri.amit.sampleapp.model.Province
import metri.amit.sampleapp.repository.CountryRepo

/**
 * Created by amitmetri on 28,April,2021
 */
class ItemDetailsViewModel(application: Application) : AndroidViewModel(application) {
    fun getProvinceList(countryId: String?): LiveData<List<Province>> {
        return CountryRepo.getInstance().getProvinceList(countryId, getApplication<Application>().applicationContext)
    }

    val errorDataMutableLiveData: MutableLiveData<ErrorData>
        get() = CountryRepo.getInstance().errorDataForItemDetailsFragment
}