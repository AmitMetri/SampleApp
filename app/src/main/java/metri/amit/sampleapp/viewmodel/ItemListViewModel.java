package metri.amit.sampleapp.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import metri.amit.sampleapp.model.Country;
import metri.amit.sampleapp.model.ErrorData;
import metri.amit.sampleapp.repository.CountryRepo;

public class ItemListViewModel extends AndroidViewModel {
    public ItemListViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Country>> getCountryList() {
        return CountryRepo.getInstance().getCountries(getApplication().getApplicationContext());
    }

    public LiveData<ErrorData> getErrorDataMutableLiveData() {
        return CountryRepo.getInstance().getErrorDataForItemListFragment();
    }
}