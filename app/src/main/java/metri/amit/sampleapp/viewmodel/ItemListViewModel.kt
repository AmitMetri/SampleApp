package metri.amit.sampleapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import metri.amit.sampleapp.model.Country
import metri.amit.sampleapp.model.ErrorData
import metri.amit.sampleapp.repository.CountryRepo

/**
 * Created by amitmetri on 28,April,2021
 */
class ItemListViewModel(application: Application) : AndroidViewModel(application) {
    val countryList: LiveData<List<Country>>
        get() = CountryRepo.getInstance().getCountries(getApplication<Application>().applicationContext)

    val errorDataMutableLiveData: LiveData<ErrorData>
        get() = CountryRepo.getInstance().errorDataForItemListFragment
}