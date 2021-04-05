package metri.amit.sampleapp.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import metri.amit.sampleapp.model.ErrorData;
import metri.amit.sampleapp.model.Province;
import metri.amit.sampleapp.repository.CountryRepo;

/**
 * Created by amitmetri on 04,April,2021
 */
public class ItemDetailsViewModel extends AndroidViewModel {

    public ItemDetailsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Province>> getProvinceList(String countryId) {
        return CountryRepo.getInstance().getProvinceList(countryId, getApplication().getApplicationContext());
    }

    public MutableLiveData<ErrorData> getErrorDataMutableLiveData() {
        return CountryRepo.getInstance().getErrorDataForItemDetailsFragment();
    }

}
