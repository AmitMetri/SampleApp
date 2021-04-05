package metri.amit.sampleapp.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import metri.amit.sampleapp.ApiClient;
import metri.amit.sampleapp.R;
import metri.amit.sampleapp.SingleMutableLiveData;
import metri.amit.sampleapp.model.Country;
import metri.amit.sampleapp.model.ErrorData;
import metri.amit.sampleapp.model.Province;

/**
 * Created by amitmetri on 04,April,2021
 */
public class CountryRepo {

    private static CountryRepo instance;
    private static String TAG = "CountryRepo";
    /*
     * Country list mutable live data
     * is being observed by the ItemListFragment
     * to show the list of countries
     * */
    MutableLiveData<List<Country>> countries = new MutableLiveData<>();
    /*
     * error mutable live data is being observed by the ItemListFragment
     * to update the UI in case of failure.
     * */
    SingleMutableLiveData<ErrorData> errorDataForItemListFragment = new SingleMutableLiveData<>();
    /*
     * error mutable live data is being observed by the ItemDetailsFragment
     * to update the UI in case of failure.
     * */
    SingleMutableLiveData<ErrorData> errorDataForItemDetailsFragment = new SingleMutableLiveData<>();

    /*
     * Singleton method to provide the instance of the repository
     * */
    public static synchronized CountryRepo getInstance() {
        if (instance == null)
            instance = new CountryRepo();
        return instance;
    }

    /*
     * ItemListFragment < ViewModel < Repository (Error data)
     * */
    public SingleMutableLiveData<ErrorData> getErrorDataForItemListFragment() {
        return errorDataForItemListFragment;
    }


    /*
     * ItemDetailsFragment < ViewModel < Repository (Error data)
     * */
    public SingleMutableLiveData<ErrorData> getErrorDataForItemDetailsFragment() {
        return errorDataForItemDetailsFragment;
    }

    /*
     * Helper method to check the connectivity
     * */
    public boolean isConnectedToNetwork(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e);
        }
        return true;
    }


    /*
     * Using Retrofit with RxJava2CallAdapterFactory.
     * Observable item is subscriber on the worker thread (Schedulers.io()).
     * Observable item is validated on the worker thread (Schedulers.computation()).
     * This makes the item available for updating the UI directly.
     * Observer is posting the item on main thread (AndroidSchedulers.mainThread())
     * */
    public LiveData<List<Country>> getCountries(Context applicationContext) {

        if (countries.getValue() == null) {
            if (isConnectedToNetwork(applicationContext)) {
                ApiClient.getClient(applicationContext).create(CountryServices.class).getCountriesList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .map(countries -> {
                            try {
                                if (countries.isEmpty()) {
                                    errorDataForItemListFragment.postValue(
                                            new ErrorData("050",
                                                    applicationContext.getString(R.string.no_countries)));
                                } else {
                                    for (Country country : countries) {
                                        if (country.getId() == null
                                                || country.getName() == null
                                                || country.getName().isEmpty()) {
                                            errorDataForItemListFragment.setValue(
                                                    new ErrorData("050",
                                                            applicationContext.getString(R.string.generic_error)));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error: " + e);
                                errorDataForItemListFragment.setValue(
                                        new ErrorData("050",
                                                applicationContext.getString(R.string.generic_error)));
                            }
                            return countries;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSingleObserver<List<Country>>() {
                            @Override
                            public void onSuccess(List<Country> value) {
                                countries.setValue(value);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "Error: " + e);
                                errorDataForItemListFragment.setValue(
                                        new ErrorData("050",
                                                applicationContext.getString(R.string.generic_error)));
                            }
                        });

            } else {
                errorDataForItemListFragment.setValue(
                        new ErrorData("050",
                                applicationContext.getString(R.string.no_network)));
            }
        }
        return countries;
    }

    /*
     * Using Retrofit with RxJava2CallAdapterFactory.
     * Observable item is subscriber on the worker thread (Schedulers.io()).
     * Observable item is validated on the worker thread (Schedulers.computation()).
     * This makes the item available for updating the UI directly.
     * Observer is posting the item on main thread (AndroidSchedulers.mainThread())
     * */
    public LiveData<List<Province>> getProvinceList(String countryId, Context applicationContext) {
        /*
         * province list mutable live data
         * which is being observed by ItemDetailsFragment
         * */
        MutableLiveData<List<Province>> provinceMutableLiveData = new MutableLiveData<>();

        if (isConnectedToNetwork(applicationContext)) {
            ApiClient.getClient(applicationContext).create(CountryServices.class).getProvinceList(countryId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .map(provinceList -> {
                        try {
                            if (provinceList.isEmpty()) {
                                errorDataForItemDetailsFragment.postValue(
                                        new ErrorData("050",
                                                applicationContext.getString(R.string.no_provinces)));
                            } else {
                                for (Province province : provinceList) {
                                    if (province.getId() == null
                                            || province.getName() == null
                                            || province.getName().isEmpty()) {
                                        errorDataForItemDetailsFragment.setValue(
                                                new ErrorData("050",
                                                        applicationContext.getString(R.string.generic_error)));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error: " + e);
                            errorDataForItemDetailsFragment.setValue(
                                    new ErrorData("050",
                                            applicationContext.getString(R.string.generic_error)));
                        }
                        return provinceList;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<List<Province>>() {
                        @Override
                        public void onSuccess(List<Province> value) {
                            provinceMutableLiveData.setValue(value);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "Error: " + e);
                            errorDataForItemDetailsFragment.setValue(
                                    new ErrorData("050",
                                            applicationContext.getString(R.string.generic_error)));
                        }
                    });
        } else {
            errorDataForItemDetailsFragment.setValue(
                    new ErrorData("050",
                            applicationContext.getString(R.string.no_network)));
        }
        return provinceMutableLiveData;
    }
}
